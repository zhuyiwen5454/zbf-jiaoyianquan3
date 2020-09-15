package com.zbf.user.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.util.Md5Utils;
import com.zbf.common.entity.AllRedisKeyEnum;
import com.zbf.common.utils.*;
import com.zbf.user.api.GetResCodeDao;
import com.zbf.user.entity.BaseUser;
import com.zbf.user.service.IBaseUserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/7  23:54
 * 描述:
 */
@RestController
public class UserController {

    @Autowired
    private GetResCodeDao getCodeDao;

    @Value("${active.path}")
    private String activePath;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private IBaseUserService baseUserService;

    private static final Pattern PATTERN_PHONE = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    // 手机验证正则比对
    public boolean isPhone(String phone){
        return PATTERN_PHONE.matcher(phone).matches();
    }

    /**
     * 向手机号发送验证码
     * @param tel
     */
    @RequestMapping("getResCode")
    public void getResCode(@RequestParam("tel") String tel){
        if (isPhone(tel)){
            System.out.println(tel);
            getCodeDao.sendCode(tel);
        }
    }

    /**
     * 下一步功能,判断输入验证码和redis中的验证码是否一致
     * @param tel
     * @param code
     * @return
     */
    @RequestMapping("first")
    public boolean first(String tel,String code){
        // 从redis中获取去code值
        String code1 = redisTemplate.opsForValue().get("code");
        // 判断从前端传过来的值是否跟redis里存入的值相同
        // 如果相同删除redis中的数据
        if(code.equals(code1)){
            /*System.out.println("成功");
            redisTemplate.delete("code");*/
            return true;
        }else{
            // 不一样就不通过
            System.out.println("失败!手机号或验证码错误");
            return false;
        }
    }

    /**
     * 注册
     * @param baseUser
     * @return
     */
    @RequestMapping("register")
    public boolean register(@RequestBody BaseUser baseUser){
        System.out.println("走进注册方法");
        // 获取前台传过来的密码
        String passWord = baseUser.getPassWord();
        // 密码加严
        baseUser.setSalt("wts");
        String pw = passWord+"wts";
        // 密码加密
        String md5 = Md5Utils.getMD5(pw, "utf8");
        // 存入加密的密码
        baseUser.setPassWord(md5);
        // 添加用户数据
        // 通过用户名查询用户信息
        BaseUser loginName = baseUserService.getBaseUserByLoginName(baseUser.getLoginName());
        // 通过邮箱查询用户信息
        BaseUser email = baseUserService.getBaseUserByEmail(baseUser.getEmail());
        // 通过手机号查询用户信息
        BaseUser phone = baseUserService.getBaseUserByTel(baseUser.getTel());
        // 判断如果手机号和邮箱登录名都不存在的时候才可以注册否则返回false
        baseUser.setStatus(0);
        if(loginName == null && email == null && phone == null){
            // 向数据库添加数据
            baseUserService.save(baseUser);
            // 获取登录的名字
            String loginName1 = baseUser.getLoginName();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 注册成功发送,向qq邮箱发送邮件,邮箱内容是一个链接
                    MailQQUtils.sendMessage(baseUser.getEmail(),"WTS商城",getActivePath(1*60*1000L,loginName1));
                }
            });
            thread.start();
            return true;
        }else{
            return false;
        }
    }

    /**
     * 作者: WTS
     * 日期: 2020/9/10  15:13
     * 参数：
     * 返回值：
     * 描述: 激活账户
     */
    @RequestMapping("activeUser")
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println("-------------------------------");
        //获取激活的串
        String actived = request.getParameter("actived");
        System.out.println(actived+"-----------");
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        //设置响应头的格式
        response.setContentType("text/html;charset=UTF-8");
        //解析激活串
        try{
            JSONObject jsonObject = JwtUtilsForOther.decodeJwtTocken(actived);

            JSONObject info = JSON.parseObject(jsonObject.getString("info"));

            //获取存储的激活码
            String code = redisTemplate.opsForValue().get(AllRedisKeyEnum.ACTIVIVE_KEY.getKey() + "_" + info.get("loginName"));

            //激活成功后跳转到激活成功页面
            //在激活成功的页面可以跳转到登录界面，进行登录
            //如果激活码正确
            if(info.get("code").equals(code)){
                String loginName = request.getParameter("loginName");
                BaseUser base = baseUserService.getBaseUserByLoginName(loginName);
                base.setStatus(1);
                baseUserService.updateById(base);
                stringObjectHashMap.put("loginPath","http://localhost:8080/");
                FreemarkerUtils.getStaticHtml(RestController.class,"/template/","activeOk.html",stringObjectHashMap,response.getWriter());
            }else{
                String loginName = request.getParameter("loginName");
                stringObjectHashMap.put("newActiveLink","http://192.168.197.1:10000/user/getNewActiveLink?loginName="+loginName);
                FreemarkerUtils.getStaticHtml(RestController.class,"/template/","activeError.html",stringObjectHashMap,response.getWriter());
            }

        }catch (ExpiredJwtException e){
            HashMap<String, Object> newData = new HashMap<>();
            String loginName = request.getParameter("loginName");
            newData.put("newActiveLink","http://192.168.245.1:10000/user/getNewActiveLink?loginName="+loginName);
            FreemarkerUtils.getStaticHtml(RestController.class,"/template/","activeError.html",newData,response.getWriter());
        }
    }

    /**
     * 作者: WTS
     * 日期: 2020/9/10  15:46
     * 参数：baseActivePath 激活的基本路劲，激活信息,timeOut 有效期
     * 返回值：
     * 描述: 这是一个普通的方法用来生成激活链接
     */
    public String getActivePath(long timeOut,String loginName){
        // 激活信息
        String code = UID.getUUID16();
        //放入redis 中
        String key=AllRedisKeyEnum.ACTIVIVE_KEY.getKey()+"_"+loginName;
        redisTemplate.opsForValue().set(key,code);
        // 设置redis的key的过期时间
        redisTemplate.expire(key,timeOut, TimeUnit.MILLISECONDS);
        // 生成激活的链接地址
        StringBuffer stringBuffer = new StringBuffer(activePath);
        stringBuffer.append("?");
        stringBuffer.append("loginName="+loginName);
        stringBuffer.append("&");
        stringBuffer.append("actived=");
        Map<String,String> map = new HashMap<>();
        map.put("loginName",loginName);
        map.put("code",code);
        stringBuffer.append(JwtUtilsForOther.generateToken(JSON.toJSONString(map),timeOut));
        String path = stringBuffer.toString();
        stringBuffer = null;
        return path;
    }

    /**
     * 作者: WTS
     * 日期: 2020/9/14  9:24
     * 描述: 激活失败重新获取激活链接邮件
     * @Param [request, response]
     * @Return void
     */
    @RequestMapping("getNewActiveLink")
    public void getNewActiveLink(HttpServletRequest request,HttpServletResponse response) throws Exception {

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();

        //设置响应头的格式
        response.setContentType("text/html;charset=UTF-8");

        //如果jwt过期，重新的发激活邮件
        String loginName = request.getParameter("loginName");
        //根据loginName获取用户信息
        BaseUser bul = baseUserService.getBaseUserByLoginName(loginName);
        //3、扣扣邮箱发送激活邮件
        MailQQUtils.sendMessage(bul.getEmail(),"WTS商城",getActivePath(1*60*1000L,loginName));

        FreemarkerUtils.getStaticHtml(RestController.class,"/template/","sendOK.html",stringObjectHashMap,response.getWriter());
    }

    /**
     * 判断是手机还是邮箱
     * @param telEmail
     * @return
     */
    @RequestMapping("getForgetCode")
    public boolean firstForget(String telEmail){
        // 通过手机号查询用户信息
        BaseUser phone = baseUserService.getBaseUserByTel(telEmail);
        // 通过邮箱查询用户信息
        BaseUser email = baseUserService.getBaseUserByEmail(telEmail);
        if(phone != null){
            if(RanDomUtils.isPhone(telEmail)){
                getCodeDao.sendCode(telEmail);
            }
        }else if (email != null){
            if(RanDomUtils.isEmail(telEmail)){
                /**
                 * 如果不是手机号就向邮箱发送验证码,调用发送邮箱MailQQUtils的sendCode方法发送验证码
                 */
                String fourRandom = RanDomUtils.getFourRandom();
                redisTemplate.opsForValue().set("code",fourRandom);
                MailQQUtils.sendCode(telEmail,fourRandom,"http://localhost:9090");
            }
        }else{
            return false;
        }
        return false;
    }

    /**
     * 忘记密码下一步功能,判断输入验证码和redis中的验证码是否一致
     * @param telEmail
     * @param code
     * @return
     */
    @RequestMapping("forgetFirst")
    public boolean forgetFirst(String telEmail,String code){
        System.out.println(code);
        System.out.println(telEmail);
        // 从redis中获取去code值
        String code1 = redisTemplate.opsForValue().get("code");
        // 判断从前端传过来的值是否跟redis里存入的值相同
        // 如果相同删除redis中的数据
        if(code.equals(code1)){
            /*System.out.println("成功");
            redisTemplate.delete("code");*/
            return true;
        }else{
            // 不一样就不通过
            System.out.println("失败!手机号或验证码错误");
            return false;
        }
    }

    /**
     * 修改密码方法
     * @param
     * @return
     */
    @RequestMapping("updatePassword")
    public boolean updatePassword(@RequestBody BaseUser baseUser){
        System.out.println("----------------修改密码");

        // 判断是邮箱还是手机号
        if(RanDomUtils.isPhone(baseUser.getTel())){
            // 如果是手机号,通过手机号查询用户信息
            BaseUser phone = baseUserService.getBaseUserByTel(baseUser.getTel());
            // 获取到用户信息的id
            Long id = phone.getId();
            // 将id存到对象中
            baseUser.setId(id);
            // 从对象中获取严值
            String salt = phone.getSalt();
            // 拼接密码
            String npwd = baseUser.getPassWord()+salt;
            // 密码加密,因为之前注册的时候密码是加密的
            String md5npwd = Md5Utils.getMD5(npwd, "utf-8");
            // 将加密的密码存到对象的密码中
            baseUser.setPassWord(md5npwd);
            // 通过id修改密码
            baseUserService.upd(baseUser);
            return true;

            // 判断是不是邮箱
        }else if(RanDomUtils.isEmail(baseUser.getTel())){
            // 如果是邮箱就通过邮箱查询用户工的信息
            BaseUser email = baseUserService.getBaseUserByEmail(baseUser.getTel());
            // 获取到用户id
            Long id = email.getId();
            // 将id存到对象中
            baseUser.setId(id);
            // 获取严值
            String salt = email.getSalt();
            // 拼接密码+严值
            String npwd = baseUser.getPassWord()+salt;
            // 密码加密,因为之前注册的时候密码也是加密的
            String md5npwd = Md5Utils.getMD5(npwd, "utf-8");
            // 将加密完的密码存到对象中
            baseUser.setPassWord(md5npwd);
            // 通过id修改密码
            baseUserService.upd(baseUser);
            return true;
        }
        return false;
    }
}

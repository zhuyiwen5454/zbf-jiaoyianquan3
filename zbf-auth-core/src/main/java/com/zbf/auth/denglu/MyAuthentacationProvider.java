package com.zbf.auth.denglu;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.util.Md5Utils;
import com.zbf.auth.mapper.MenuDao;
import com.zbf.auth.mapper.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/8/26 21:43
 * 描述: 这个类主要是用用来认证 密码的  有验证码什么的也可在此认证
 */

@Component
public class MyAuthentacationProvider implements AuthenticationProvider {

    @Autowired
    private MyUserServiceDetail myUserServiceDetail;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MenuDao menuDao;

    private static final Pattern PATTERN_PHONE = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    private static final Pattern PATTERN_EMAIL = Pattern.compile("^\\w+@\\w+([-]\\w+)*(\\.\\w+)+$");

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //获取表单的用户名
        String userName = authentication.getPrincipal().toString();
        System.out.println("++++++"+userName);
        //获取用户填写的密码
        String password = authentication.getCredentials().toString();
        // MD5加密
        Md5Utils.getMD5(password,"utf-8");
        System.out.println("++++++"+password);


        /**
         * 手机号验证
         */
        if(isPhone(userName)){
            System.out.println("执行了手机验证的操作");
            // 从Redis中获取code存的验证码
            String code = redisTemplate.opsForValue().get("code");
            System.out.println(code+"-----------------");
            if(code.equals(password)){
                System.out.println("进入判断");
                //根据用户名获取用户的信息，这里调用根据用户名获取用户信息的UserServiceDetail类
                UserDetails userDetails = myUserServiceDetail.loadUserByUsername(userName);
                //根据获取的用户信息获取用户的密码
                String password1 = userDetails.getPassword();
                //查询所有的权限是不是在Redis，如果不在Redis的话，从数据库查询然后更新到Redis
                if(!redisTemplate.hasKey("menuRole")){
                    System.out.println("是否有redis");
                    List<Map<String, String>> allMenus = menuDao.getAllMenus();
                    Map<String,String> urlRole=new HashMap<>();
                    allMenus.forEach(mapp->{
                        urlRole.put(mapp.get("urlRoleCode"),"");
                    });
                    //将所有的权限存入Redis
                    redisTemplate.opsForHash().putAll("menuRole",urlRole);
                    redisTemplate.expire("menuRole",60*60,TimeUnit.SECONDS);
                }
                //说明：
                //这里一步--》密码校验成功后可以加载一下 当前用户的权限角色 放入缓存中方便鉴权的时候使用
                //userDetails.getAuthorities();//这里存储的是当前用户的角色信息
                //缓存用户的权限信息
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, password1, userDetails.getAuthorities());
                String string = JSON.toJSONString(usernamePasswordAuthenticationToken);
                //将当前用户的角色信息 放入缓存大key user-auth 小key用户登录名获取权限，value用户的角色列表
                redisTemplate.opsForHash().put("user-auth",userDetails.getUsername(),string);
                return usernamePasswordAuthenticationToken;
            }else{
                throw new BadCredentialsException("验证码过期或错误");
            }
        }else if(isEmail(userName)){
            System.out.println("执行了邮箱验证的操作");
            // 从Redis中获取code存的验证码
            String code = redisTemplate.opsForValue().get("code");
            System.out.println(code+"-----------------");
            if(code.equals(password)){
                System.out.println("进入判断");
                //根据用户名获取用户的信息，这里调用根据用户名获取用户信息的UserServiceDetail类
                UserDetails userDetails = myUserServiceDetail.loadUserByUsername(userName);
                //根据获取的用户信息获取用户的密码
                String password1 = userDetails.getPassword();
                //查询所有的权限是不是在Redis，如果不在Redis的话，从数据库查询然后更新到Redis
                if(!redisTemplate.hasKey("menuRole")){
                    System.out.println("是否有redis");
                    List<Map<String, String>> allMenus = menuDao.getAllMenus();
                    Map<String,String> urlRole=new HashMap<>();
                    allMenus.forEach(mapp->{
                        urlRole.put(mapp.get("urlRoleCode"),"");
                    });
                    //将所有的权限存入Redis
                    redisTemplate.opsForHash().putAll("menuRole",urlRole);
                    redisTemplate.expire("menuRole",60*60,TimeUnit.SECONDS);
                }
                //说明：
                //这里一步--》密码校验成功后可以加载一下 当前用户的权限角色 放入缓存中方便鉴权的时候使用
                //userDetails.getAuthorities();//这里存储的是当前用户的角色信息
                //缓存用户的权限信息
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, password1, userDetails.getAuthorities());
                String string = JSON.toJSONString(usernamePasswordAuthenticationToken);
                //将当前用户的角色信息 放入缓存大key user-auth 小key用户登录名获取权限，value用户的角色列表
                redisTemplate.opsForHash().put("user-auth",userDetails.getUsername(),string);
                return usernamePasswordAuthenticationToken;
            }else{
                throw new BadCredentialsException("验证码过期或错误");
            }
        }
        else{
            //加密密码
            BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
            //String encode = bCryptPasswordEncoder.encode(password);
            //根据用户名获取用户的信息，这里调用根据用户名获取用户信息的UserServiceDetail类
            UserDetails userDetails = myUserServiceDetail.loadUserByUsername(userName);
            System.out.println("==============");
            // 通过登录名查询用户信息
            Map<String, Object> userByUserName = userDao.getUserByUserName(userName);
            // 获取严值
            String salt = userByUserName.get("salt").toString();
            // 拼接密码加严值
            String pwd = password + salt;
            // MD5加密
            String newPassword = Md5Utils.getMD5(pwd, "utf-8");
            //根据获取的用户信息获取用户的密码
            String password1 = userDetails.getPassword();
            //bCryptPasswordEncoder这个类加密的密码每次是不一样的，所以要比较原始密码的话
            //只能使用bCryptPasswordEncoder.matches方法进行比较
            //这里就是认证了 ^_^ !
            if (!newPassword.equals(password1)){
                throw new BadCredentialsException("用户名或密码不正确");
            }

            //查询所有的权限是不是在Redis，如果不在Redis的话，从数据库查询然后更新到Redis
            if(!redisTemplate.hasKey("menuRole")){
                List<Map<String, String>> allMenus = menuDao.getAllMenus();
                Map<String,String> urlRole=new HashMap<>();
                allMenus.forEach(mapp->{
                    urlRole.put(mapp.get("urlRoleCode"),"");
                });
                //将所有的权限存入Redis
                redisTemplate.opsForHash().putAll("menuRole",urlRole);
                redisTemplate.expire("menuRole",60*60,TimeUnit.SECONDS);
            }
            //说明：
            //这里一步--》密码校验成功后可以加载一下 当前用户的权限角色 放入缓存中方便鉴权的时候使用
            //userDetails.getAuthorities();//这里存储的是当前用户的角色信息
            //缓存用户的权限信息
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, password1, userDetails.getAuthorities());
            String string = JSON.toJSONString(usernamePasswordAuthenticationToken);
            //将当前用户的角色信息 放入缓存大key user-auth 小key用户登录名authentication.getPrincipal()，value用户的角色列表
            redisTemplate.opsForHash().put("user-auth",authentication.getPrincipal().toString(),string);
            return usernamePasswordAuthenticationToken;
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    public boolean isPhone(String phone){
        return PATTERN_PHONE.matcher(phone).matches();
    }

    // 邮箱验证正则比对
    public boolean isEmail(String email){
        return PATTERN_EMAIL.matcher(email).matches();
    }
}

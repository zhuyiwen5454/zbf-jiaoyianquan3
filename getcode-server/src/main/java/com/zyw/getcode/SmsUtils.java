package com.zyw.getcode;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zbf.common.utils.MailQQUtils;
import com.zbf.common.utils.RanDomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: zhuyiwen
 * @Date: 2018/5/29 10:49
 * @Description: 短信验证码测试
 */
@RestController
public class SmsUtils {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @RequestMapping("sendCode")
    public boolean sendCode(@RequestParam("tel") String tel) throws ClientException {
        // 手机号
        System.out.println("tel++++"+tel);
        // 生成随机数
        String s = RanDomUtils.getFourRandom();
        // 存入redis
        redisTemplate.opsForValue().set("code",s);

        // 产品名称:云通信短信API产品,开发者无需替换
        String product = "Dysmsapi";
        // 产品域名,开发者无需替换
        String domain = "dysmsapi.aliyuncs.com";

        // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
        String accessKeyId = "LTAI4G7PRsRwyJt9BT1e3m64";           // TODO 改这里
        String accessKeySecret = "tLCXv4wxU7Knay7jha2lweNHdE1Ftb"; // TODO 改这里

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "300000");
        System.setProperty("sun.net.client.defaultReadTimeout", "300000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(tel);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("惠宜家商城"); // TODO 改这里
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_202560023");  // TODO 改这里
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的用户,您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"" + s + "\"}");

        System.out.println(s+"----------------------------------");


        // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode()!= null && sendSmsResponse.getCode().equals("OK")){
            System.out.println(sendSmsResponse.getCode());
            System.out.println("验证码发送成功!");
            return true;
        }
        return false;
    }
}
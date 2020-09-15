package com.zbf.common.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 作者: LCG
 * 日期: 2020/6/15 15:44
 * 描述: 邮件发送工具类
 */
public class Mail126Utils {

    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 描述: 邮件服务器的地址
     */
    public static final String HOST = "smtp.126.com";
    /**
     * 描述: 邮件协议
     */
    public static final String PROTOCOL = "smtp";
    /**
     * 描述: 邮件服务器端口
     */
    public static final int PORT = 25;
    /**
     * 描述: 发件方
     */
    public static final String FROM = "@126.com";
    //授权码
    public static final String PWD = "";

    public static void sendMail(String to,String webName,String linkherf){
        //配置邮箱服务器
        Properties props=new Properties();
        props.put("mail.smtp.host", HOST);//设置服务器地址
        props.put("mail.store.protocol" , PROTOCOL);//设置协议
        props.put("mail.smtp.port", PORT);//设置端口
        props.put("mail.smtp.auth" , true);
        //1.创建连接对象，连接到邮箱服务器
        Session session=Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }
        });
        //2.创建邮件对象
        Message message=new MimeMessage(session);
        try {
            //设置发件人
            message.setFrom(new InternetAddress(FROM));
            //设置收件人
            message.setRecipient(RecipientType.TO, new InternetAddress(to));
            //设置邮件的主题
            message.setSubject(webName+"账户激活邮件");
            //设置邮件的正文
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("<h2>亲爱的用户：</h2>");
            stringBuffer.append("<p>您好！系统监测到您在 "+simpleDateFormat.format(new Date())+" 注册成为了三豆网络的VIP用户，非常感谢您对我们的信赖，下面是您的账户的激活地址,请点击或者复制该链接到浏览器中完成激活操作</p>");
            stringBuffer.append("<p><a herf='"+linkherf+"'>"+linkherf+"</a></p>");
            stringBuffer.append("<p>请您在24小时之内完成激活操作，否做该链接就会过期</p>");
            message.setContent(stringBuffer.toString(),"text/html;charset=UTF-8");
            //3.发送激活邮件
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*public static void main(String[] args) {
        sendMail("2433929346@qq.com","三豆网络","http://localhost:9090");
    }*/

}

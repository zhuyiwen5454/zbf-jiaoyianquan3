package com.zbf.common.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.Writer;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2018/12/5 19:36
 * 描述：生成模板的工具方法
 */
public class FreemarkerUtils {

    /**
     *  //生成静态页面
     * @param clazz           调用该工具类的Class
     * @param templatePath    模板所在的相对目录
     * @param templateName    模板的名称
     * @param map             模板中的参数
     */
    public static void getStaticHtml(Class clazz,String templatePath,
            String templateName,Map<String,Object> map,Writer writer) throws Exception {
         // 创建配置
         Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
         //加载模板的路径
         configuration.setClassForTemplateLoading ( clazz,templatePath);
         // 设置默认编码为UTF-8
         configuration.setDefaultEncoding("UTF-8");

         Template template= configuration.getTemplate ( templateName );

         template.process ( map,writer );
         writer.close ();
         writer=null;
         configuration=null;

    }

}

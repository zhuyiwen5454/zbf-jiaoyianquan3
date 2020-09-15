package com.zbf.auth.config;

import com.zbf.auth.denglu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/6  22:39
 * 描述: 这是Security的安全登录配置类
 */
@EnableWebSecurity
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 作者: LCG
     * 日期: 2020/9/7  22:29
     * 参数：
     * 返回值：
     * 描述: 登陆成功处理器
     */
    @Autowired
    private MyLoginSuccessHandler myLoginSuccessHandler;

    /**
     * 作者: LCG
     * 日期: 2020/9/7  22:29
     * 参数：
     * 返回值：
     * 描述: 登录失败处理器
     */
    @Autowired
    private MyLoginFailureHandler myLoginFailureHandler;

    /**
     * 作者: LCG
     * 日期: 2020/9/7  22:29
     * 参数：
     * 返回值：
     * 描述: 访问拒绝处理器
     */
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    /**
     * 作者: LCG
     * 日期: 2020/9/7  22:30
     * 参数：
     * 返回值：
     * 描述: 登录认证处服务
     */
    @Autowired
    private MyAuthentacationProvider myAuthentacationProvider;

    /**
     * 作者: LCG
     * 日期: 2020/9/7  22:30
     * 参数：
     * 返回值：
     * 描述: 维持登录状态的过滤器
     */
    @Autowired
    private MyAuthentactionAccessTokenFilter myAuthentactionAccessTokenFilter;

    @Value("${auth.loginPage}")
    private String loginPage;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.   //认证请求
                authorizeRequests()
                //匹配这些路径
                .antMatchers("/oauth/**", "/actuator/**", "/forLogin", "/loginPage","/getCode")
                //无需认证
                .permitAll()
                //其余的任何请求
                .anyRequest()
                //需要认证后才可以访问
                .authenticated()
                .and()
                .formLogin()
                .loginPage(loginPage)
                .permitAll()
                .and()
                .exceptionHandling()
                //访问拒绝的处理
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .addFilterAt(getCustomAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(myAuthentactionAccessTokenFilter,UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(myAuthentacationProvider);
        auth.eraseCredentials(false);
    }


    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public CustomAuthenticationFilter getCustomAuthenticationFilter(AuthenticationManager authenticationManager){
        CustomAuthenticationFilter customAuthenticationFilter=new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager);
        //设置的登录路径,这里设置的话 上边的HttpSecurity中就不用设置了
        customAuthenticationFilter.setFilterProcessesUrl("/forLogin");
        customAuthenticationFilter.setAuthenticationFailureHandler(myLoginFailureHandler);
        customAuthenticationFilter.setAuthenticationSuccessHandler(myLoginSuccessHandler);
        return customAuthenticationFilter;
    }


    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

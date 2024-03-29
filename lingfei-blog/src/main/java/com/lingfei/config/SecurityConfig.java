package com.lingfei.config;


import com.lingfei.filter.JwtAuthenticationTokenFilter;
import com.lingfei.handler.security.AccessDeniedHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //注入登录校验过滤器
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    //认证异常处理器
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    //授权异常处理器
    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //放行controller的登录接口
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext，因为是前后端分离
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/login").anonymous()
                //指定该api需要认证校验！
                .antMatchers("/logout").authenticated()
                .antMatchers("/user/userInfo").authenticated()
                .antMatchers(HttpMethod.POST,"/comment").authenticated()
                //除上面外的所有请求不需认证即可访问！
               .anyRequest().permitAll();
                // 除上面外的所有请求全部需要鉴权认证
//                       .anyRequest().authenticated();

        //允许跨域
        http.cors();
        //关闭默认的注销功能
        http.logout().disable();
        //把token校验过滤器添加到过滤器链中，目标是在UsernamePasswordAuthenticationFilter.class之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }

    //重写方法来暴露AuthenticationManager这个bean，注入容器
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
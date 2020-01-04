package com.sso.zjx.crm.config;

import com.sso.zjx.crm.listener.MySessionListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class MyConfig {

    @Bean
    public ServletListenerRegistrationBean myListener(){
        ServletListenerRegistrationBean registrationBean = new ServletListenerRegistrationBean(new MySessionListener());
        return registrationBean;
    }
}

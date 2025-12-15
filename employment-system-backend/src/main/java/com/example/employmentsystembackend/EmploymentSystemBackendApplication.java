package com.example.employmentsystembackend; // 👈 修正这里：必须和你实际的文件夹名字一样

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
// 👇 加这行保险：扫描所有 com.example 下的组件，防止因为包名不一样导致 Controller 找不到
@ComponentScan("com.example")
public class EmploymentSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmploymentSystemBackendApplication.class, args);
    }

    // 全局跨域配置
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
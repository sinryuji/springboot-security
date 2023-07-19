package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests()
        .antMatchers("/user/**").authenticated()
        .antMatchers("/manager/**").hasRole("ADMIN or MANAGER")
        .antMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().permitAll()
        .and()
        .formLogin().loginPage("/login")
        .and().build();

  }
}

package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UtilConfig {

  // 해당 메서드의 리턴되는 오브젝트를 IOC로 등록 해준다.
  @Bean
  public BCryptPasswordEncoder encodePwd() {
    return new BCryptPasswordEncoder();
  }
}

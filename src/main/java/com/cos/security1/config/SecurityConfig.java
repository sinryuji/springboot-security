package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// Secured 어노테이션 활성화, // PreAuthorize, PostAuthorize 어노테이션 활성화
public class SecurityConfig {

  @Autowired
  private PrincipalOauth2UserService principalOauth2UserService;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf().disable()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
        .authorizeHttpRequests()
        .antMatchers("/user/**").authenticated()
        .antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
        .antMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().permitAll()
        .and()
        .formLogin().loginPage("/loginForm")
//        .usernameParameter("nickname") // UserDetailsService 구현체에서 구현할 loadUserByUsername의 파라미터로 들어갈 username을 다른걸로 변경 할 수 있다.
        .loginProcessingUrl("/login")
        .defaultSuccessUrl("/")
        .and()
        .oauth2Login()//.loginPage("/loginForm")
        // 구글 로그인이 완료된 뒤의 후처리가 필요함. Tip. 로그인이 완료되면 코드를 받지 않음. (액세스 토큰 + 사용자 프로필 정보를 한 번에 받음)
        .userInfoEndpoint()
        .userService(principalOauth2UserService) // OAuth 로그인 성공 후에 인자로 넣어준 Service에서 설정을 진행하겠다.
        .and().and().build();
  }
}

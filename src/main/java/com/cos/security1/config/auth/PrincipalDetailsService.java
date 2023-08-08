package com.cos.security1.config.auth;

import com.cos.security1.entity.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessUrl("/login");으로 걸어놨기 때문에
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  // Security session(내부 Authentication(내부 UserDetails))
  // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 붙은 객체가 만들어진다.
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user != null) {
      return new PrincipalDetails(user);
    }
    return null;
  }
}

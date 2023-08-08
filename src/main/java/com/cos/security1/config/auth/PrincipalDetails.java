package com.cos.security1.config.auth;

import com.cos.security1.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

// 시큐리티가 /login을 낚아채서 로그인을 진행시킴
// 로그인이 완료가 되면 시큐리티 session(시큐리티만이 사용하는 Security ContextHolder)을 만들어준다.
// Security ContextHolder에 들어갈 수 있는 오브젝트가 정해져 있음(Authentication)
// Authentication 안에 User정보가 있어야 됨
// User 오브젝트의 타입은 UserDetails 타입 객체여야 됨
// Security Session => Authentication => UserDetails
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

  private final User user; // 컴포지션

  private Map<String, Object> attributes;

  // 일반 로그인
  public PrincipalDetails(User user) {
    this.user = user;
  }

  // OAuth 로그인
  public PrincipalDetails(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  // 해당 유저의 권한을 리턴하는 곳!!
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collect = new ArrayList<>();
    collect.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return user.getRole();
      }
    });
    return collect;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  // 계정이 만료가 안됐나?
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // 계정이 잠기지 않았나?
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // 계정이 너무 오래돼지 않았나?
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // 계정이 사용 가능한가?
  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public String getName() {
    return null;
  }
}

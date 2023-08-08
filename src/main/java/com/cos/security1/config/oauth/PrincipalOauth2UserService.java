package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.GitHubUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.entity.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  // 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
  // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 붙은 객체가 만들어진다.
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
    System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());

    OAuth2User oAuth2User = super.loadUser(userRequest);

    // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
    // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필을 받음
    System.out.println("getAttributes: " + oAuth2User.getAttributes());

    OAuth2UserInfo oAuth2UserInfo = null;
    if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
      oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
    } else if (userRequest.getClientRegistration().getRegistrationId().equals("github")) {
      oAuth2UserInfo = new GitHubUserInfo(oAuth2User.getAttributes());
    } else {
      System.out.println("우리는 구글과 깃허브만 지원해여 ㅎㅎ");
    }

    String provider = oAuth2UserInfo.getProvider();
    String providerId = oAuth2UserInfo.getProviderId();
    String username = provider + "_" + providerId;
    String password = bCryptPasswordEncoder.encode("겟인데어");
    String email = oAuth2UserInfo.getEmail();
    String role = "ROLE_USER";

    User user = userRepository.findByUsername(username);

    if (user == null) {
      user = User.builder()
          .username(username)
          .password(password)
          .email(email)
          .role(role)
          .provider(provider)
          .providerId(providerId)
          .build();
      userRepository.save(user);
    }

    return new PrincipalDetails(user, oAuth2User.getAttributes());
  }
}

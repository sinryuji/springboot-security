package com.cos.security1.controller;

import com.cos.security1.entity.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping({"", "/"})
  public String index() {
    // 머스테치 기본 폴더 src/main/resources/
    // 뷰리졸버 설정: templates(prefix), .mustache(suffix)
    return "index"; // src/main/resources/templates/index.mustache
  }

  @GetMapping("/user")
  public @ResponseBody String user() {
    return "user";
  }

  @GetMapping("/admin")
  public @ResponseBody String admin() {
    return "admin";
  }

  @GetMapping("/manager")
  public @ResponseBody String manager() {
    return "manager";
  }

  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

  @GetMapping("/joinForm")
  public String joinForm() {
    return "joinForm";
  }

  @PostMapping("/join")
  public String join(User user) {
    System.out.println(user);
    user.setRole("ROLE_USER");
    String rawPassword = user.getPassword();
    String encPassword = bCryptPasswordEncoder.encode(rawPassword);
    user.setPassword(encPassword);
    userRepository.save(user); // 그냥 이대로 하면 시큐리티로 로그인 할 수 없음. 이유는 패스워드가 암호화가 안되어있기 때문.
    return "redirect:/loginForm";
  }

  @Secured({"ROLE_ADMIN", "ROLE_MANAGER"}) // 간단하게 해당 URL에 ROLE 검증 가능
  @GetMapping("/info")
  public @ResponseBody String info() {
    return "개인정보";
  }

  @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
  // 해당 메서드가 호출 되기 전에 검사를 진행하는 PreAuthorize 어노테이션을 통해서도 권한을 검사 가능
  @GetMapping("/data")
  public @ResponseBody String data() {
    return "데이터 정보";
  }
}

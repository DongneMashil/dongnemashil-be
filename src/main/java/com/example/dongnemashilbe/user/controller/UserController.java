package com.example.dongnemashilbe.user.controller;


import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.UserinfoDto;
import com.example.dongnemashilbe.user.dto.SignupRequestDto;
import com.example.dongnemashilbe.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
//    private final KakaoService kakaoService;


    @PostMapping("/register")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto, HttpServletResponse response) {
        logger.error("회원가입 시도");
        logger.error("이메일 : "+signupRequestDto.getEmail());
        logger.error("닉네임 : "+signupRequestDto.getNickname());
        logger.error("비밀번호 : " + signupRequestDto.getPassword());

        userService.signup(signupRequestDto,response);


        return "회원가입 성공";
    }


    @GetMapping("/accesstoken")
    public UserinfoDto getAccessToken(@AuthenticationPrincipal UserDetailsImpl userDetails){
        logger.error("토큰인증 성공");
        return new UserinfoDto(userDetails.getUser().getEmail(),userDetails.getUser().getNickname());

    }

    @GetMapping("/refreshtoken")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response){
        logger.error("리프레쉬 토큰 발급 시도");

        userService.getRefreshToken(request,response);

    }

}

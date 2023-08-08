package com.example.dongnemashilbe.user.controller;


import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.*;
import com.example.dongnemashilbe.user.service.KakaoService;
import com.example.dongnemashilbe.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;


    @PostMapping("/register")
    public SuccessMessageDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto, HttpServletResponse response) {

        userService.signup(signupRequestDto,response);


        return new SuccessMessageDto("회원가입 성공");
    }

    @PostMapping("/kakao")
    public SuccessMessageDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 JWT 반환
        kakaoService.kakaoLogin(response , code);

        return new SuccessMessageDto("로그인 성공");
    }

    @GetMapping("/accesstoken")
    public UserinfoDto getAccessToken(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new UserinfoDto(userDetails.getUser().getEmail(),userDetails.getUser().getNickname());

    }

    @GetMapping("/refreshtoken")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response){

        userService.getRefreshToken(request,response);

    }

    @PostMapping("/register/nickname")
    public SuccessMessageDto checkedNickname(@RequestBody NicknameRequestDto nicknameRequestDto){
        return userService.checkedNickname(nicknameRequestDto);
    }

    @PostMapping("/register/email")
    public SuccessMessageDto checkedEmail(@RequestBody EmailRequestDto emailRequestDto){
        return userService.checkedEmail(emailRequestDto);
    }

    @GetMapping("/logout")
    public SuccessMessageDto logout( HttpServletResponse response){
        userService.logout(response);
        return new SuccessMessageDto("로그아웃이 완료되었습니다.");

    }
}

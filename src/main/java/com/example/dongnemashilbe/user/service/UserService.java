package com.example.dongnemashilbe.user.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.security.jwt.JwtUtil;
import com.example.dongnemashilbe.user.dto.SignupRequestDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.entity.UserRoleEnum;
import com.example.dongnemashilbe.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto signupRequestDto, HttpServletResponse response) {
        String email = signupRequestDto.getEmail();
        String nickname = signupRequestDto.getNickname();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        //사용자 이메일 중복확인
        Optional<User> checkUsername = userRepository.findByEmail(email);
        if(checkUsername.isPresent()){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        //사용자 닉네임 중복확인
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if(checkNickname.isPresent()){
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        // 사용자등록
        User user = new User(email,password,nickname,UserRoleEnum.USER);
        userRepository.save(user);

        String accessToken = jwtUtil.createAccessToken(email,UserRoleEnum.USER);
        String refreshToken = jwtUtil.createRefreshToken(email);

        jwtUtil.addJwtToCookie(JwtUtil.ACCESSTOKEN_HEADER,accessToken,response);
        jwtUtil.addJwtToCookie(JwtUtil.REFRESHTOKEN_HEADER,refreshToken,response);
    }

    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.getTokenFromRequest(JwtUtil.REFRESHTOKEN_HEADER,request);

        String tokenValue = jwtUtil.substringToken(token);

        String email = jwtUtil.getUserInfoFromToken(tokenValue).getSubject();

        String accessToken = jwtUtil.createAccessToken(email,UserRoleEnum.USER);

        jwtUtil.addJwtToCookie(JwtUtil.ACCESSTOKEN_HEADER,accessToken,response);
    }
}

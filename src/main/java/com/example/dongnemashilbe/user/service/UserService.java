package com.example.dongnemashilbe.user.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.security.jwt.JwtUtil;
import com.example.dongnemashilbe.user.dto.EmailRequestDto;
import com.example.dongnemashilbe.user.dto.NicknameRequestDto;
import com.example.dongnemashilbe.user.dto.SignupRequestDto;
import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.entity.UserRoleEnum;
import com.example.dongnemashilbe.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

        String accessToken = jwtUtil.createAccessToken(nickname,UserRoleEnum.USER);
        String refreshToken = jwtUtil.createRefreshToken(nickname);

        jwtUtil.addJwtToHeader(JwtUtil.ACCESSTOKEN_HEADER,accessToken,response);
        jwtUtil.addJwtToHeader(JwtUtil.REFRESHTOKEN_HEADER,refreshToken,response);
    }

    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        if (request.getHeader(JwtUtil.REFRESHTOKEN_HEADER) == null)
            throw new CustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN);
        String token =URLDecoder.decode(request.getHeader(JwtUtil.REFRESHTOKEN_HEADER), "UTF-8");

        String tokenValue = jwtUtil.substringToken(token);
        try{
            jwtUtil.validateToken(tokenValue);
        }catch (ExpiredJwtException e){
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }catch (SecurityException | MalformedJwtException | SignatureException e) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String nickname = jwtUtil.getUserInfoFromToken(tokenValue).getSubject();

        String accessToken = jwtUtil.createAccessToken(nickname,UserRoleEnum.USER);

        jwtUtil.addJwtToHeader(JwtUtil.ACCESSTOKEN_HEADER,accessToken,response);
    }

    public SuccessMessageDto checkedNickname(NicknameRequestDto nicknameRequestDto) {
        if(userRepository.findByNickname(nicknameRequestDto.getNickname()).isPresent())
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        return new SuccessMessageDto("사용가능한 닉네임 입니다.");
    }

    public SuccessMessageDto checkedEmail(EmailRequestDto emailRequestDto) {
        if(userRepository.findByEmail(emailRequestDto.getEmail()).isPresent())
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        return new SuccessMessageDto("사용가능한 닉네임 입니다.");
    }
}

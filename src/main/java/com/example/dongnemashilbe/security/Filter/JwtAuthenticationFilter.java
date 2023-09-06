package com.example.dongnemashilbe.security.Filter;

import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.security.jwt.JwtUtil;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.LoginRequestDto;
import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.entity.UserRoleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            // AuthenticationManager 가 인증처리
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)throws IOException {
        String nickname = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createAccessToken(nickname,role);
        String refreshToken = jwtUtil.createRefreshToken(nickname);

        jwtUtil.addJwtToHeader(JwtUtil.ACCESSTOKEN_HEADER,accessToken,response);
        jwtUtil.addJwtToHeader(JwtUtil.REFRESHTOKEN_HEADER,refreshToken,response);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(new SuccessMessageDto("로그인 성공"));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(userJson);


    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(400);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(ErrorCode.INVALID_EMAIL_PASSWORD.getMessage());
    }

}

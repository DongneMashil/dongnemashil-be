package com.example.dongnemashilbe.security.Filter;

import com.example.dongnemashilbe.security.jwt.JwtUtil;
import com.example.dongnemashilbe.security.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = jwtUtil.getTokenFromRequest(JwtUtil.ACCESSTOKEN_HEADER,req);


            if (StringUtils.hasText(token) && token != null) {

                String tokenValue = jwtUtil.substringToken(token);

                jwtUtil.validateToken(tokenValue);

                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    logger.error("인증오류!!!!");
                }
            }
        }catch (Exception e){
            req.setAttribute("exception",e);
        }
        logger.error(req.getRequestURI());
        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String nickname) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = null;
        try{
           authentication = createAuthentication(nickname);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String nickname) {
        UserDetails userDetails = userDetailsService.loadUserByNickname(nickname);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
package com.example.dongnemashilbe.security.jwt;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.user.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Accesstoken Header KEY 값
    public static final String ACCESSTOKEN_HEADER = "Accesstoken";

    // Refreshtoken Header KEY 값
    public static final String REFRESHTOKEN_HEADER = "Refreshtoken";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long ACCESSTOKEN_TIME =  15 * 60 * 1000L; // 15분
    private final long REFRESHTOKEN_TIME = 60 * 60 * 1000L; // 60분 -- > 1시간

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    // @PostConstruct : 딱 한번 받아와야 하는 값을 사용 할때마다 요청을 새로 호출하는 실수를 방지하기 위해 사용
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 엑세스 토큰 생성
    public String createAccessToken(String nickname, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(nickname)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESSTOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 리프세쉬 토큰 생성
    public String createRefreshToken(String nickname) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(nickname)
                        .setExpiration(new Date(date.getTime() + REFRESHTOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // JWT Cookie 에 저장
    public void addJwtToCookie(String header,String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            String headerValue = "; Path=/; Secure; HttpOnly; SameSite=None; Max-Age=840";


            if(header.equals(REFRESHTOKEN_HEADER)){
                headerValue = "; Path=/; Secure; HttpOnly; SameSite=None; Max-Age=3600";
            }

            res.addHeader("Set-Cookie", header+"="+token+headerValue);

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage()+"쿠키 전달 실패");
        }
    }

    
    public void logout(String header,String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            String headerValue = "; Path=/; Secure; HttpOnly; SameSite=None; Max-Age=0";

            res.addHeader("Set-Cookie", header+"="+token+headerValue);

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage()+"쿠키 전달 실패");
        }
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        //StringUtils.hasText(tokenValue) ==> 공백인지 null 인지 체크
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    // 토큰에서 사용자 정보 가져오기
    // 키 확인작업
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(String tokenHeader, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenHeader)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
            throw new CustomException(ErrorCode.NOT_FOUND_TOKEN);
        }
        return null;
    }

}

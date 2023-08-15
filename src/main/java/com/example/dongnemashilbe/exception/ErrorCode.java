package com.example.dongnemashilbe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //회원가입
    EMAIL_DIFFERENT_FORMAT(HttpStatus.BAD_REQUEST.value(), "이메일 형식이 일치하지 않습니다."),
    NICKNAME_DIFFERENT_FORMAT(HttpStatus.BAD_REQUEST.value(), "닉네임 형식이 일치하지 않습니다."),
    PASSWORD_DIFFERENT_FORMAT(HttpStatus.BAD_REQUEST.value(), "비밀번호 형식이 일치하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "이미 이메일이 존재합니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "이미 닉네임이 존재합니다."),

    //로그인
    INVALID_EMAIL_PASSWORD(HttpStatus.BAD_REQUEST.value(), "잘못된 이메일이거나 잘못된 비밀번호입니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST.value(), "사용자를 찾을 수 없습니다."),


    //로그인 유저 정보
//    NULLABLE(HttpStatus.BAD_REQUEST.value(), "Nullable=false"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "토큰 유효기간 만료."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효한 토큰이 아닙니다."),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED.value(),"로그인 이후에 이용이 가능합니다."),

    //리뷰(게시글) 리스트 // 리뷰(게시글) 작성
    OUT_OF_RANGE(HttpStatus.BAD_REQUEST.value(), "범위를 벗어난 페이지 요청입니다."),
    REVIEW_NOT_EXIST(HttpStatus.NOT_FOUND.value(), "리뷰가 존재하지 않습니다."),
    TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요한 기능입니다."),
    NOT_THE_AUTHOR(HttpStatus.FORBIDDEN.value(), "작성자가 아닙니다."),
    ELEMENTS_IS_REQUIRED(HttpStatus.BAD_REQUEST.value(), "필수값이 생략되었습니다."),
    UPLOAD_FAILED(HttpStatus.BAD_REQUEST.value(), "파일 업로드를 실패했습니다."),
    UNSUPPORTED_MEDIA_type(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "지원하지 않는 파일 형식입니다."),

    COMMENT_NOT_EXIST(HttpStatus.NOT_FOUND.value(), "댓글이 존재하지 않습니다."),
    UNEXPECTED_ERROR(443,"예기치못한 오류"),

    ;

    private final int httpStatus;
    private final String message;

}
package com.example.dongnemashilbe.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "이메일 형식이 맞지 않습니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "패스워드 형식이 맞지 않습니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,15}$",message = "비밀번호는 최소 8자리 이상이며, 영문 숫자, 특수문자(!@#$%^&*)가 모두 포함되어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임 형식이 맞지 않습니다.")
    @Pattern(regexp = "^[a-z0-9가-힣]{2,10}$")
    private String nickname;
}

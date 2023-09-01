package com.example.dongnemashilbe.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequestDto {

    @NotBlank(message = "이메일 형식이 맞지 않습니다.")
    @Email
    private String email;

}

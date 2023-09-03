package com.example.dongnemashilbe.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class NicknameRequestDto {

    @NotBlank(message = "닉네임 형식이 맞지 않습니다.")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ0-9가-힣]{2,10}$")
    private String nickname;

}

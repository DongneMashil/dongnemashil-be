package com.example.dongnemashilbe.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserinfoDto {
    private String email;
    private String nickname;


    public UserinfoDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}

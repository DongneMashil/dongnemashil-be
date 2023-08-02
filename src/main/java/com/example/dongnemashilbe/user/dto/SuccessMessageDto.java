package com.example.dongnemashilbe.user.dto;

import lombok.Getter;

@Getter
public class SuccessMessageDto {

    private String message;

    public SuccessMessageDto(String message) {
        this.message = message;
    }
}

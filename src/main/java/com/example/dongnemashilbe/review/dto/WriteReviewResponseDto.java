package com.example.dongnemashilbe.review.dto;

import lombok.Getter;


@Getter
public class WriteReviewResponseDto {

    private Long id;

    public WriteReviewResponseDto(Long id) {
        this.id = id;
    }
}

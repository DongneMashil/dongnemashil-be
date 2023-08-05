package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;

@Getter
public class WriteReviewResponseDto {

    private Long id;


    public WriteReviewResponseDto(Review review) {
        this.id = review.getId();

    }
}

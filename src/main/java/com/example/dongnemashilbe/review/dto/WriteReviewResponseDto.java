package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;

import java.util.List;

@Getter
public class WriteReviewResponseDto {

    private Long id;


    public WriteReviewResponseDto(Long id) {
        this.id = id;
    }
}

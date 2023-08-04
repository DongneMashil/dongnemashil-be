package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Like;
import lombok.Getter;

@Getter
public class LikeResponseDto {
    private Long user_id;
    private Long review_id;



    public LikeResponseDto(Like like){
        this.user_id = like.getUser().getId();
        this.review_id = like.getReview().getId();
    }
}

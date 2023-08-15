package com.example.dongnemashilbe.user.dto;

import com.example.dongnemashilbe.like.entity.Like;
import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyPageListResponseDto {

    private Long reviewId;
    private String imgUrl;
    private String roadName;
    private String userprofileUrl;
    private LocalDateTime createdAt;


    public MyPageListResponseDto(Like like) {
        this.reviewId = like.getReview().getId();
        this.imgUrl = like.getReview().getMainImgUrl();
        this.roadName = like.getReview().getRoadName();
        this.userprofileUrl = like.getReview().getUser().getProfileImgUrl();
    }

    public MyPageListResponseDto(Review review) {
        this.reviewId = review.getId();
        this.imgUrl = review.getMainImgUrl();
        this.roadName = review.getRoadName();
        this.createdAt = review.getCreatedAt();
    }
}

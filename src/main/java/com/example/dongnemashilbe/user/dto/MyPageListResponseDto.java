package com.example.dongnemashilbe.user.dto;

import com.example.dongnemashilbe.review.entity.Like;
import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyPageListResponseDto {

    private Long reviewId;
    private String imgUrl;
    private String address;
    private String userprofileUrl;
    private LocalDateTime createdAt;


    public MyPageListResponseDto(Like like) {
        this.reviewId = like.getReview().getId();
        this.imgUrl = like.getReview().getMainImgUrl();
        this.address = like.getReview().getAddress();
        this.userprofileUrl = like.getReview().getUser().getProfileImgUrl();
    }

    public MyPageListResponseDto(Review review) {
        this.reviewId = review.getId();
        this.imgUrl = review.getMainImgUrl();
        this.address = review.getAddress();
        this.createdAt = review.getCreatedAt();
    }
}

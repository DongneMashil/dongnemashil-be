package com.example.dongnemashilbe.review.dto;


import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainPageReviewResponseDto {
    private Long id;
    private String roadName;
    private String imgUrl;
    private String profileImgUrl;
    private Integer likeCnt;


    public MainPageReviewResponseDto(Review review, Integer likeCount){
        this.id = review.getId();
        this.roadName = review.getRoadName();
        this.imgUrl = review.getImgUrl();
        this.likeCnt = likeCount;
        this.profileImgUrl = review.getProfileImgUrl();

    }
}

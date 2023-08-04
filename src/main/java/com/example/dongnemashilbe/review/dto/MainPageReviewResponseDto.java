package com.example.dongnemashilbe.review.dto;


import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainPageReviewResponseDto {
    private Long id;
    private String roadName;
    private String img_url;
    private String profileImg_url;
    private String tag;
    private Integer likeCnt;


    public MainPageReviewResponseDto(Review review, Integer likeCount){
        this.id = review.getId();
        this.roadName = review.getRoadName();
        this.img_url = review.getImg_url();
        this.tag = review.getTag();
        this.likeCnt = likeCount;
        this.profileImg_url = review.getProfileImg_url();

    }
}

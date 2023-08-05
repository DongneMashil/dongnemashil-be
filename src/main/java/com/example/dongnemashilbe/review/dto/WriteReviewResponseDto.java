package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;

@Getter
public class WriteReviewResponseDto {

    private Long id;
    private String title;
    private String nickname;
    private String content;
    private String imgUrl;
    private String videoUrl;
    private String roadName;
    private String tag;
    private String address;

    public WriteReviewResponseDto(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.imgUrl = review.getImgUrl();
        this.videoUrl = review.getVideoUrl();
        this.address = review.getAddress();
        this.nickname = review.getUser().getNickname();
        this.roadName = review.getRoadName();
        this.tag = review.getTag();
    }
}

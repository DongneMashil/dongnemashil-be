package com.example.dongnemashilbe.review.dto;

import lombok.Getter;

@Getter
public class WriteReviewRequestDto {

    private String title;
    private String content;
    private String imgUrl;
    private String videoUrl;
    private String address;
    private String tag;
    private String roadName;

}

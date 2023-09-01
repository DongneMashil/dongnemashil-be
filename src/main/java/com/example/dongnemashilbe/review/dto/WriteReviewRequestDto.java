package com.example.dongnemashilbe.review.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class WriteReviewRequestDto {

    private String title;
    private String content;
    private String address;
    private List<String> tag;
    private String roadName;
    private double latitude;
    private double longitude;
}

package com.example.dongnemashilbe.review.dto;

import lombok.Getter;

import java.util.List;

@Getter

public class DetailPageRequestDto {
    private String content;
    private String mainImgUrl;
    private List<String> subImgUrl;
    private String videoUrl;
    private String roadName;
    private String profileImgUrl;
    private String nickname;
    private String address;
    private String title;
    private List<String> tag;
    private Integer likeCnt;
    private Integer commentCnt;

}

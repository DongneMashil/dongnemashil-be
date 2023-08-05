package com.example.dongnemashilbe.review.dto;

import lombok.Getter;

@Getter

public class DetailPageRequestDto {
    private String content;
    private String imgUrl;
    private String videoUrl;
    private String profileImgUrl;
    private String nickname;
    private String address;
    private String title;
    private String tag;
    private Integer likeCnt;
    private Integer commentCnt;

}

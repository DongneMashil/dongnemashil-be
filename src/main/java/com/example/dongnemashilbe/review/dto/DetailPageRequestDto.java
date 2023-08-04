package com.example.dongnemashilbe.review.dto;

import lombok.Getter;

@Getter

public class DetailPageRequestDto {
    private String content;
    private String img_url;
    private String video_url;
    private String profileImg_url;
    private String nickname;
    private String address;
    private String title;
    private String tag;
    private Integer likeCnt;
    private Integer commentCnt;

}

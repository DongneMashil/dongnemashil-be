package com.example.dongnemashilbe.review.dto;

import lombok.Getter;

@Getter
public class MediaUrlsDto {
    private final String mainImgUrl;
    private final String subImageUrlsString;
    private final String videoUrl;

    public MediaUrlsDto(String mainImgUrl, String subImageUrlsString, String videoUrl){
        this.mainImgUrl = mainImgUrl;
        this.subImageUrlsString = subImageUrlsString;
        this.videoUrl = videoUrl;
    }
}

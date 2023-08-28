package com.example.dongnemashilbe.review.dto;

import lombok.Getter;

@Getter
public class MediaUrlsDto {
    private final String mainImgUrl;
    private final String subImageUrlsString;
    private final String middleSubImageUrlsString;
    private final String smallSubImageUrlsString;
    private final String videoUrl;

    public MediaUrlsDto(String mainImgUrl, String subImageUrlsString,String middleSubImageUrlsString,String smallSubImageUrlsString, String videoUrl){
        this.mainImgUrl = mainImgUrl;
        this.subImageUrlsString = subImageUrlsString;
        this.videoUrl = videoUrl;
        this.middleSubImageUrlsString=middleSubImageUrlsString;
        this.smallSubImageUrlsString=smallSubImageUrlsString;
    }
}

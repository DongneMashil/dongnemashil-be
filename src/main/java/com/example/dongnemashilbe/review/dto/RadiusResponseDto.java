package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;


@Getter
public class RadiusResponseDto {

    private Long id;
    private String nickname;
    private String roadName;
    private String address;
    private String smallMainImgUrl;
    public double latitude;
    public double longitude;

    public RadiusResponseDto(Review review) {
        this.id = review.getId();
        this.nickname = review.getUser().getNickname();
        this.roadName = review.getRoadName();
        this.address = review.getAddress();
        this.smallMainImgUrl = review.getSmallMainImgUrl();
        this.latitude= review.getLatitude();
        this.longitude= review.getLongitude();
    }
}

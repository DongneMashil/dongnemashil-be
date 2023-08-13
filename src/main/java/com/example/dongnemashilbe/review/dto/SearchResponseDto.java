package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;

@Getter
public class SearchResponseDto {

    private Long id;
    private String roadName;
    private String nickname;
    private String mainImgUrl;
    private String profileImgUrl;
    private Integer likeCnt;
    private boolean likebool;
    private String address;

    public SearchResponseDto(Review review, Integer likeCount, String mainImgUrl, boolean likebool) {
        this.id = review.getId();
        this.roadName = review.getRoadName();
        this.nickname =review.getUser().getNickname();
        this.likeCnt = likeCount;
        this.profileImgUrl = review.getUser().getProfileImgUrl();
        this.mainImgUrl = mainImgUrl;
        this.likebool = likebool;
        this.address=review.getAddress();
    }
}

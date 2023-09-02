package com.example.dongnemashilbe.review.dto;


import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class MainPageReviewResponseDto implements Serializable {
    private Long id;
    private String nickname;
    private String roadName;
    private String middleMainImgUrl;
    private String smallMainImgUrl;
    private String profileImgUrl;
    private Integer likeCnt;
    private boolean likebool;
    private LocalDateTime createdAt;

    public MainPageReviewResponseDto(Review review, Integer likeCount, boolean likebool){
        this.id = review.getId();
        this.nickname=review.getUser().getNickname();
        this.roadName = review.getRoadName();
        this.likeCnt = likeCount;
        this.profileImgUrl = review.getUser().getProfileImgUrl();
        this.middleMainImgUrl = review.getMiddleMainImgUrl();
        this.smallMainImgUrl=review.getSmallMainImgUrl();
        this.likebool = likebool;
        this.createdAt=review.getCreatedAt();

    }
}

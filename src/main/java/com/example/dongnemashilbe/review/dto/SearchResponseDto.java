package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SearchResponseDto implements Serializable {

    private Long id;
    private String roadName;
    private String nickname;
    private String middleMainImgUrl;
    private String smallMainImgUrl;
    private String profileImgUrl;
    private Integer likeCnt;
    private boolean likebool;
    private String address;
    private LocalDateTime createdAt;

    public SearchResponseDto(Review review, Integer likeCount, boolean likebool) {

        this.id = review.getId();
        this.roadName = review.getRoadName();
        this.nickname =review.getUser().getNickname();
        this.likeCnt = likeCount;
        this.profileImgUrl = review.getUser().getProfileImgUrl();
        this.middleMainImgUrl = review.getMiddleMainImgUrl();
        this.smallMainImgUrl=review.getSmallMainImgUrl();
        this.likebool = likebool;
        this.address=review.getAddress();
        this.createdAt=review.getCreatedAt();
    }

}

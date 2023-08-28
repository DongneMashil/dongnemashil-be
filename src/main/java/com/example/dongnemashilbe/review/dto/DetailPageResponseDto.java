package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DetailPageResponseDto {

    private Long id;
    private String content;
    private String nickname;
    private String profileImgUrl;
    private String address;
    private String roadName;
    private String title;
    private String mainImgUrl;
    private String middleMainImgUrl;
    private String smallMainImgUrl;
    private List<String> subImgUrl = new ArrayList<>();
    private List<String> middleSubImgUrl = new ArrayList<>();
    private List<String> smallSubImgUrl = new ArrayList<>();
    private String videoUrl;
    private Integer likeCnt;
    private Integer commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<TagResponseDto> tag;
    private boolean likebool;


    public DetailPageResponseDto(Review review,Integer likeCount,Long commentCnt, boolean likebool){
        this.id=review.getId();
        this.content=review.getContent();
        this.nickname=review.getUser().getNickname();
        this.profileImgUrl=review.getUser().getProfileImgUrl();
        this.address=review.getAddress();
        this.roadName=review.getRoadName();
        this.title=review.getTitle();
        this.likeCnt=likeCount;
        this.commentCnt= Math.toIntExact(commentCnt);
        this.createdAt=review.getCreatedAt();
        this.modifiedAt=review.getModifiedAt();
        this.tag = review.getReview_tagList().stream()
                .map(review_tag -> new TagResponseDto(review_tag.getTag()))
                .collect(Collectors.toList());
        this.likebool = likebool;
        this.mainImgUrl = review.getMainImgUrl();
        this.middleMainImgUrl = review.getMiddleMainImgUrl();
        this.smallMainImgUrl=review.getSmallMainImgUrl();

        for (String sub : review.getSubImgUrl().split(",")) {
            this.subImgUrl.add(sub) ;
        }
        for (String sub : review.getMiddleSubImgUrl().split(",")) {
            this.middleSubImgUrl.add(sub) ;
        }
        for (String sub : review.getSmallSubImgUrl().split(",")) {
            this.smallSubImgUrl.add(sub) ;
        }
        this.videoUrl = review.getVideoUrl();
    }
}

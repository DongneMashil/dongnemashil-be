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
    private String title;
    private String mainImgUrl;
    private List<String> subImgUrl = new ArrayList<>();
    private String videoUrl;
    private Integer likeCnt;
    private Integer commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<TagResponseDTO> tag;
    private boolean likebool;


    public DetailPageResponseDto(Review review, Integer likeCount, Long commentCnt,
                                 String mainImgUrl, String subImgUrl, String videoUrl){
        this.id=review.getId();
        this.content=review.getContent();
        this.nickname=review.getUser().getNickname();
        this.profileImgUrl=review.getUser().getProfileImgUrl();
        this.address=review.getAddress();
        this.title=review.getTitle();
        this.likeCnt=likeCount;
        this.commentCnt= Math.toIntExact(commentCnt);
        this.createdAt=review.getCreatedAt();
        this.modifiedAt=review.getModifiedAt();
        this.tag = review.getReview_tagList().stream()
                .map(review_tag -> new TagResponseDTO(review_tag.getTag()))
                .collect(Collectors.toList());
        this.mainImgUrl = mainImgUrl;

        for (String sub : subImgUrl.split(",")) {
            this.subImgUrl.add(sub) ;
        }
        this.videoUrl = videoUrl;

    }
    public DetailPageResponseDto(Review review,Integer likeCount,Long commentCnt,
                                 String mainImgUrl, String subImgUrl,String videoUrl, boolean likebool){
        this.id=review.getId();
        this.content=review.getContent();
        this.nickname=review.getUser().getNickname();
        this.profileImgUrl=review.getUser().getProfileImgUrl();
        this.address=review.getAddress();
        this.title=review.getTitle();
        this.likeCnt=likeCount;
        this.commentCnt= Math.toIntExact(commentCnt);
        this.createdAt=review.getCreatedAt();
        this.modifiedAt=review.getModifiedAt();
        this.tag = review.getReview_tagList().stream()
                .map(review_tag -> new TagResponseDTO(review_tag.getTag()))
                .collect(Collectors.toList());
        this.likebool = likebool;
        this.mainImgUrl = mainImgUrl;

        for (String sub : subImgUrl.split(",")) {
            this.subImgUrl.add(sub) ;
        }
        this.videoUrl = videoUrl;
    }
}

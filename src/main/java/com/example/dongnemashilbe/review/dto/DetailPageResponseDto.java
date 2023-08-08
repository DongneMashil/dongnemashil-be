package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DetailPageResponseDto {

    private Long id;
    private String content;
    private String imgUrl;
    private String videoUrl;
    private String profileImgUrl;
    private String address;
    private String title;
    private Integer likeCnt;
    private Integer commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<TagResponseDTO> tag;
    private boolean likebool;

    public DetailPageResponseDto(Review review){
        this.id=review.getId();
        this.content=review.getContent();
        this.imgUrl=review.getImgUrl();
        this.videoUrl=review.getVideoUrl();
        this.profileImgUrl=review.getUser().getProfileImgUrl();
        this.address=review.getAddress();
        this.title=review.getTitle();
        this.createdAt=review.getCreatedAt();
        this.modifiedAt=review.getModifiedAt();
        this.tag = review.getReview_tagList().stream()
                .map(review_tag -> new TagResponseDTO(review_tag.getTag()))
                .collect(Collectors.toList());


    }
    public DetailPageResponseDto(Review review,Integer likeCount,Long commentCnt){
        this.id=review.getId();
        this.content=review.getContent();
        this.imgUrl=review.getImgUrl();
        this.videoUrl=review.getVideoUrl();
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

    }
    public DetailPageResponseDto(Review review,Integer likeCount,Long commentCnt, boolean likebool){
        this.id=review.getId();
        this.content=review.getContent();
        this.imgUrl=review.getImgUrl();
        this.videoUrl=review.getVideoUrl();
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
    }
}

package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.comment.dto.CommentResponseDto;
import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DetailPageResponseDto {

    private Long id;
    private String content;
    private String imgUrl;
    private String videoUrl;
    private String profileImgUrl;
    private String nickname;
    private String address;
    private String title;
    private Integer likeCnt;
    private Integer commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String tag;

    public DetailPageResponseDto(Review review){
        this.id=review.getId();
        this.content=review.getContent();
        this.imgUrl=review.getImgUrl();
        this.videoUrl=review.getVideoUrl();
        this.profileImgUrl=review.getProfileImgUrl();
        this.address=review.getAddress();
        this.title=review.getTitle();
        this.createdAt=review.getCreatedAt();
        this.modifiedAt=review.getModifiedAt();
        this.tag=review.getTag();
    }
    public DetailPageResponseDto(Review review,Integer likeCount,Long commentCnt){
        this.id=review.getId();
        this.content=review.getContent();
        this.imgUrl=review.getImgUrl();
        this.videoUrl=review.getVideoUrl();
        this.profileImgUrl=review.getProfileImgUrl();
        this.address=review.getAddress();
        this.title=review.getTitle();
        this.likeCnt=likeCount;
        this.commentCnt= Math.toIntExact(commentCnt);
        this.createdAt=review.getCreatedAt();
        this.modifiedAt=review.getModifiedAt();
        this.tag=review.getTag();
    }
}

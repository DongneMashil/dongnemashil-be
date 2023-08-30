package com.example.dongnemashilbe.user.dto;

import com.example.dongnemashilbe.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyCommentResponseDto {
    private Long id;
    private String comment;
    private String nickname;
    private String profileImgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long reviewId;


    public MyCommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
        this.profileImgUrl = comment.getReview().getSmallMainImgUrl();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt=comment.getModifiedAt();
        this.reviewId=comment.getReview().getId();
    }
}

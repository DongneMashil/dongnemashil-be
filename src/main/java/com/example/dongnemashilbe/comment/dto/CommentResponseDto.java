package com.example.dongnemashilbe.comment.dto;

import com.example.dongnemashilbe.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String nickname;
    private String profileImgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
        this.profileImgUrl = comment.getUser().getProfileImgUrl();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt=comment.getModifiedAt();
    }
}

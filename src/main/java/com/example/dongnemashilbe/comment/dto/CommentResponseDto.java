package com.example.dongnemashilbe.comment.dto;

import com.example.dongnemashilbe.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String nickname;
    private String profileImgUrl;


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
        this.profileImgUrl = comment.getUser().getProfileImgUrl();
    }
}

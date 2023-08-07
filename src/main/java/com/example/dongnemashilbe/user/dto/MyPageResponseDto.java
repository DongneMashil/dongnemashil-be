package com.example.dongnemashilbe.user.dto;

import com.example.dongnemashilbe.user.entity.User;
import lombok.Getter;


@Getter
public class MyPageResponseDto {

    private String email;
    private String profileImgUrl;
    private String nickname;
    private Long writeReviewListCnt;
    private Long likeReviewListCnt;

    public MyPageResponseDto(User user) {
        this.email=user.getEmail();
        this.profileImgUrl= user.getProfileImgUrl();;
        this.nickname=user.getNickname();
        this.writeReviewListCnt= 3L;
        this.likeReviewListCnt= 3L;
    }
}

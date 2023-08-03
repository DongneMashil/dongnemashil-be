package com.example.dongnemashilbe.review.responsedto;

import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DetailPageResponseDto {

    private Long id;
    private String content;
    private String img_url;
    private String video_url;
    private String profileImg_url;
    private String nickname;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public DetailPageResponseDto(Review review){
        this.id=review.getId();
        this.content=review.getTag();
        this.img_url=review.getImg_url();
        this.video_url=review.getImg_url();
        this.profileImg_url=review.getProfileImg_url();
        this.nickname=review.getNickname();
        this.address=review.getAddress();
        this.createdAt=review.getCreatedAt();
        this.modifiedAt=review.getModifiedAt();
    }
}

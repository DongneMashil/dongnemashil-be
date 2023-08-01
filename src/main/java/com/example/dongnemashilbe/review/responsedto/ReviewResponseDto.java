package com.example.dongnemashilbe.review.responsedto;


import com.example.dongnemashilbe.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {
    private Long id;
    private String address;
    private String img_url;
    private String tag;
    private Integer likeCnt;


    public ReviewResponseDto(Review review){
        this.id = review.getId();
        this.address = review.getAddress();
        this.img_url = review.getImg_url();
        this.tag = review.getTag();
        this.likeCnt = review.getLikeCnt();
    }
}

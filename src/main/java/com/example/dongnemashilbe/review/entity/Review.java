package com.example.dongnemashilbe.review.entity;

import com.example.dongnemashilbe.comment.entity.Comment;
import com.example.dongnemashilbe.like.entity.Like;
import com.example.dongnemashilbe.review.dto.DetailPageRequestDto;
import com.example.dongnemashilbe.review.dto.WriteReviewRequestDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.global.util.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Table
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "road_name")
    private String roadName;

    @Column(nullable = false,length = 500)
    private String content;

    @Column(nullable = false)
    public String address;

    @Column(nullable = false)
    public String title;

    @Column(length = 1000)
    private String mainImgUrl;

    @Column(length = 1000)
    private String middleMainImgUrl;

    @Column(length = 1000)
    private String smallMainImgUrl;

    @Column(length = 10000)
    private String subImgUrl;

    @Column(length = 10000,columnDefinition = "TEXT")
    private String middleSubImgUrl;

    @Column(length = 10000,columnDefinition = "TEXT")
    private String smallSubImgUrl;

    @Column
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review_Tag> review_tagList = new ArrayList<>();

    public Review(WriteReviewRequestDto writeReviewRequestDto, User user, String mainImgUrl, String subImgUrl, String videoUrl) {
        this.title = writeReviewRequestDto.getTitle();
        this.content = writeReviewRequestDto.getContent();
        this.address = writeReviewRequestDto.getAddress();
        this.roadName = writeReviewRequestDto.getRoadName();
        this.videoUrl = videoUrl;
        this.mainImgUrl = mainImgUrl;
        this.subImgUrl = subImgUrl;
        this.user = user;
    }

    public Review(WriteReviewRequestDto writeReviewRequestDto, User user, String mainImgUrl, String subImageUrlsString,
                  String videoUrl, String smallMainImg, String middleMainImg,String middleSubImageUrlsString,
                  String smallSubImageUrlsString) {
        this.title = writeReviewRequestDto.getTitle();
        this.content = writeReviewRequestDto.getContent();
        this.address = writeReviewRequestDto.getAddress();
        this.roadName = writeReviewRequestDto.getRoadName();
        this.videoUrl = videoUrl;
        this.mainImgUrl = mainImgUrl;
        this.subImgUrl = subImageUrlsString;
        this.user = user;
        this.smallMainImgUrl=smallMainImg;
        this.middleMainImgUrl=middleMainImg;
        this.middleSubImgUrl=middleSubImageUrlsString;
        this.smallSubImgUrl=smallSubImageUrlsString;
    }


    public void update(DetailPageRequestDto detailPageRequestDto,User user,String mainImgUrl,
                       String subImageUrlsString, String videoUrl,String middleSubImageUrlsString,
                       String smallSubImageUrlsString, String smallMainImg, String middleMainImg) {
        this.address = detailPageRequestDto.getAddress();
        this.title = detailPageRequestDto.getTitle();
        this.content = detailPageRequestDto.getContent();
        this.videoUrl = videoUrl;
        this.mainImgUrl = mainImgUrl;
        this.subImgUrl = subImageUrlsString;
        this.user = user;
        this.middleSubImgUrl=middleSubImageUrlsString;
        this.smallSubImgUrl=smallSubImageUrlsString;
        this.smallMainImgUrl=smallMainImg;
        this.middleMainImgUrl=middleMainImg;
    }
}
package com.example.dongnemashilbe.review.entity;

import com.example.dongnemashilbe.comment.entity.Comment;
import com.example.dongnemashilbe.review.dto.DetailPageRequestDto;
import com.example.dongnemashilbe.review.dto.WriteReviewRequestDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.global.util.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private String content;

    @Column(name = "profile_img_url")
    public String profileImgUrl;

    @Column(nullable = false)
    public String address;

    @Column(nullable = false)
    public String title;

    @Column(length = 1000)
    private String mainImgUrl;

    @Column(length = 10000)
    private String subImgUrl;

    @Column
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "review")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "review")
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



    public void update(DetailPageRequestDto detailPageRequestDto,User user,String mainImgUrl, String subImageUrlsString, String videoUrl) {
        this.address = detailPageRequestDto.getAddress();
        this.title = detailPageRequestDto.getTitle();
        this.content = detailPageRequestDto.getContent();
        this.videoUrl = videoUrl;
        this.mainImgUrl = mainImgUrl;
        this.subImgUrl = subImageUrlsString;
        this.user = user;
    }
}

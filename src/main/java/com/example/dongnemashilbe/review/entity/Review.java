package com.example.dongnemashilbe.review.entity;

import com.example.dongnemashilbe.review.dto.DetailPageRequestDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.util.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Table
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roadName;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String img_url;

    @Column
    private String video_url;

    @Column(nullable = false)
    private String tag;

    @Column
    private Integer likeCnt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    public String profileImg_url;

    @Column(nullable = false)
    public String nickname;

    @Column(nullable = false)
    public String address;

    @Column(nullable = false)
    public String title;

    @Column
    public Integer commentCnt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private List<Like> like;

    public void update(DetailPageRequestDto detailPageRequestDto) {
        this.address = detailPageRequestDto.getAddress();
        this.img_url = detailPageRequestDto.getImg_url();
        this.nickname = detailPageRequestDto.getNickname();
        this.profileImg_url = detailPageRequestDto.getProfileImg_url();
        this.tag = detailPageRequestDto.getTag();
        this.title = detailPageRequestDto.getTitle();
        this.video_url = detailPageRequestDto.getVideo_url();
        this.content = detailPageRequestDto.getContent();
        this.likeCnt = detailPageRequestDto.getLikeCnt();
        this.commentCnt = detailPageRequestDto.getCommentCnt();
    }
}

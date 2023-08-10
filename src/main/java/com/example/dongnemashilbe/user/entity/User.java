package com.example.dongnemashilbe.user.entity;


import com.example.dongnemashilbe.review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(length = 1000)
    private String profileImgUrl;

    @Column
    private Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    private List<Review> reviewList = new ArrayList<>();



    public User(String email, String password, String nickname, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public User(String nickname, String encodedPassword, UserRoleEnum user, Long kakaoId, String email) {
        this.nickname = nickname;
        this.password = encodedPassword;
        this.role = user;
        this.kakaoId = kakaoId;
        this.email = email;
    }

    public void uploadUser(String nickname, String S3Url) {
        this.nickname= nickname;
        this.profileImgUrl=S3Url;
    }
}

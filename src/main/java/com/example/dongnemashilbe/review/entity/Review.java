package com.example.dongnemashilbe.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
@Table
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roadName;

    @Column
    private String img_url;

    @Column
    private String tag;

    @Column
    private Integer likeCnt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column
    public String profileImg_url;

    @Column
    public String nickname;

    @Column
    public String address;

}

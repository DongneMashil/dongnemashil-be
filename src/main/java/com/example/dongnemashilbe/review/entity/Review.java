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
    private String address;

    @Column
    private String img_url;

    @Column
    private String tag;

    @Column
    private Integer likeCnt;

    @Column(updatable = false)
    private LocalDateTime createdAt;
}

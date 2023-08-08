package com.example.dongnemashilbe.review.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String mainImgUrl;

    @Column(length = 10000)
    private String subImgUrl;

    @Column
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public MediaFile(String mainImgUrl, String subImgUrl, String videoUrl, Review review){
        this.mainImgUrl = mainImgUrl;
        this.subImgUrl = subImgUrl;
        this.videoUrl = videoUrl;
        this.review = review;

    }

    public void update(String mainImgUrl, String subImgUrl, String videoUrl) {
        if ( mainImgUrl != null){
            this.mainImgUrl = mainImgUrl;
        }
        if (videoUrl != null) {
            this.videoUrl = videoUrl;
        }
        if(subImgUrl != null){
            this.subImgUrl = subImgUrl;
        }
        if(review != null){
            this.review = review;
        }
    }

}

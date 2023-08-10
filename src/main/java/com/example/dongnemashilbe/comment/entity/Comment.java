package com.example.dongnemashilbe.comment.entity;

import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.global.util.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;


    public Comment(String comment, Review review, User user) {
        this.comment = comment;
        this.review = review;
        this.user = user;
    }

    public void update(String comment) {
        this.comment = comment;
    }
}

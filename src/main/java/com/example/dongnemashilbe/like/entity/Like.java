package com.example.dongnemashilbe.like.entity;

import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "`like`")
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;


    public Like(User user, Review review) {
        this.user = user;
        this.review = review;
    }
}

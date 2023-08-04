package com.example.dongnemashilbe.review.entity;

import com.example.dongnemashilbe.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Getter
@Setter
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

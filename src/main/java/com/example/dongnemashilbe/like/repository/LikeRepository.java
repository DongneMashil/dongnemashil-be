package com.example.dongnemashilbe.like.repository;

import com.example.dongnemashilbe.like.entity.Like;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndReview(User user, Review review);


    @Query("SELECT COUNT(l) FROM Like l WHERE l.review = :review")
    Integer countByReview(@Param("review") Review review);


    Slice<Like> findAllByUser_Id(Long id,Pageable pageable);
}

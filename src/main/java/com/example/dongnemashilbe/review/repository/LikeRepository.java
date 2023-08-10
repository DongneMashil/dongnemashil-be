package com.example.dongnemashilbe.review.repository;

import com.example.dongnemashilbe.review.entity.Like;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndReview(User user, Review review);


    @Query("SELECT COUNT(l) FROM Like l WHERE l.review = :review")
    Integer countByReview(@Param("review") Review review);

//    @Query("SELECT r FROM Review r ORDER BY SIZE(r.likes) DESC, r.id DESC")
//    Slice<Review> findAllByLikes(Pageable pageable);
    Slice<Like> findAllByUser_Id(Long id,Pageable pageable);
}

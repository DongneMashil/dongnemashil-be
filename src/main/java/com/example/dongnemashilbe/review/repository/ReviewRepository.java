package com.example.dongnemashilbe.review.repository;

import com.example.dongnemashilbe.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r ORDER BY SIZE(r.likes) DESC, r.id DESC")
    Slice<Review> findAllByLikes(Pageable pageable);


    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    Slice<Review> findAllByRecent(Pageable pageable);

    @Query("SELECT COUNT(c) from Comment c where c.review.id = :reviewId")
    Long countCommentsByReviewId(@Param("reviewId") Long reviewId);

    @Query("SELECT rt.review FROM Review_Tag rt WHERE rt.tag.name IN :tags GROUP BY rt.review ORDER BY SIZE(rt.review.likes) DESC, rt.review.id DESC")
    Slice<Review> findAllByLikesAndTags(Pageable pageable, @Param("tags") List<String> tags);

    @Query("SELECT rt.review FROM Review_Tag rt WHERE rt.tag.name IN :tags GROUP BY rt.review ORDER BY rt.review.createdAt DESC")
    Slice<Review> findAllByRecentAndTags(Pageable pageable, @Param("tags") List<String> tags);

}


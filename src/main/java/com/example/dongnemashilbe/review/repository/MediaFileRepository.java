package com.example.dongnemashilbe.review.repository;

import com.example.dongnemashilbe.review.entity.MediaFile;
import com.example.dongnemashilbe.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    Optional<MediaFile> findByReviewId(Long reviewId);

    Optional<MediaFile> findByReview(Review review);
}

package com.example.dongnemashilbe.review.repository;

import com.example.dongnemashilbe.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r ORDER BY r.likeCnt DESC, r.id DESC")
    Slice<Review> findAllByLikes(Pageable pageable);

    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    Slice<Review> findAllByRecent(Pageable pageable);

}

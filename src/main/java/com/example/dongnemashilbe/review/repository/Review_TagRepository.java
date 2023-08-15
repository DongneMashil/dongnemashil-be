package com.example.dongnemashilbe.review.repository;

import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.entity.Review_Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Review_TagRepository extends JpaRepository<Review_Tag,Long>{

    List<Review_Tag> findAllByReview(Review review);

}

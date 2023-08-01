package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Slice<Review> findAllByLikes(Pageable pageable) {
        return reviewRepository.findAllByLikes(pageable);
    }

    public Slice<Review> findAllByRecent(Pageable pageable) {
        return reviewRepository.findAllByRecent(pageable);
    }
}


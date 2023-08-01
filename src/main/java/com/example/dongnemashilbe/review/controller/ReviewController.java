package com.example.dongnemashilbe.review.controller;


import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.responsedto.ReviewResponseDto;
import com.example.dongnemashilbe.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/main/likes")
    public Slice<ReviewResponseDto> getAllReviewsByLikes(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<Review> sliceReviews = reviewService.findAllByLikes(pageable);

        return sliceReviews.map(ReviewResponseDto::new);
    }

    @GetMapping("/main/recent")
    public Slice<ReviewResponseDto> getAllReviewsByRecent(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<Review> sliceReviews = reviewService.findAllByRecent(pageable);

        return sliceReviews.map(ReviewResponseDto::new);
    }
}


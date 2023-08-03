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
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/")
    public final Slice<ReviewResponseDto> getAllReviews(
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size ) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Slice<Review> sliceReviews = reviewService.findAllByType(type, pageable);
        return sliceReviews.map(ReviewResponseDto::new);
    }
}


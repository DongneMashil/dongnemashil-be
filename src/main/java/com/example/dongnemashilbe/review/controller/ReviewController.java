package com.example.dongnemashilbe.review.controller;


import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.responsedto.DetailPageResponseDto;
import com.example.dongnemashilbe.review.responsedto.MainPageReviewResponseDto;
import com.example.dongnemashilbe.review.service.ReviewService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("")
    public Slice<MainPageReviewResponseDto> getAllReviews(
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size ) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Slice<Review> sliceReviews = reviewService.findAllByType(type, pageable);
        return sliceReviews.map(MainPageReviewResponseDto::new);
    }

    @GetMapping("/{id}")
    public DetailPageResponseDto getReview(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.getReview(id,userDetails);
    }
}


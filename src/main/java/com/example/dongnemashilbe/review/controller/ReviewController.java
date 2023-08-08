package com.example.dongnemashilbe.review.controller;


import com.example.dongnemashilbe.comment.dto.CommentRequestDto;
import com.example.dongnemashilbe.review.dto.*;
import com.example.dongnemashilbe.review.service.ReviewService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.SuccessMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Pageable pageable = PageRequest.of(page - 1, size);

        try{
            userDetails.getUser();
        }catch (NullPointerException e){
            return reviewService.findAllByType(type, pageable,null);
        }
        return reviewService.findAllByType(type, pageable,userDetails.getUser());
    }

    @GetMapping("/{id}")
    public DetailPageResponseDto getReview(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            userDetails.getUser();
        }catch (NullPointerException e){

            return reviewService.getReview(id,null);
        }

        return reviewService.getReview(id,userDetails.getUser());
    }

    @PostMapping("")
    public WriteReviewResponseDto createReview(@RequestBody WriteReviewRequestDto writeReviewRequestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.createReview(writeReviewRequestDto, userDetails.getUser());
    }

    @PutMapping("/{id}")
    public DetailPageResponseDto updateReview(@PathVariable Long id,
                                              @RequestBody DetailPageRequestDto detailPageRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
           return reviewService.updateReview(id,detailPageRequestDto,userDetails);
    }


    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        reviewService.deleteReview(id, userDetails);
    }


    @PostMapping("/{id}/likes")
    public SuccessMessageDto like(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.like(id, userDetails.getUser().getNickname());
    }
}


package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.review.dto.DetailPageRequestDto;
import com.example.dongnemashilbe.review.dto.DetailPageResponseDto;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Slice<Review> findAllByType(String type, Pageable pageable) {
        if ("likes".equals(type)) {
            return reviewRepository.findAllByLikes(pageable);
        } else if ("recent".equals(type)) {
            return reviewRepository.findAllByRecent(pageable);
        } else {
            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }
    }

    public DetailPageResponseDto getReview(Long id) {
        return new DetailPageResponseDto(reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST)));
    }

    @Transactional
    public DetailPageResponseDto updateReview(Long id,
                                              DetailPageRequestDto detailPageRequestDto,
                                              UserDetailsImpl userDetails) {
        Review review = findReviewById(id);
        validate(review, userDetails);
        review.update(detailPageRequestDto);

        return new DetailPageResponseDto(review);
    }

    public void deleteReview(Long id, UserDetailsImpl userDetails) {

        Review review = findReviewById(id);
        validate(review, userDetails);
        reviewRepository.delete(review);
    }


    private Review findReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));
    }

    private void validate(Review review, UserDetailsImpl userDetails) {

        if (!userDetails.getUser().getRole().getAuthority().equals("ROLE_ADMIN")) {
            if (!review.getUser().getId().equals(userDetails.getUser().getId())) {
                throw new CustomException(ErrorCode.NOT_THE_AUTHOR);
            }
        }
    }
}




package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.review.requestdto.DetailPageRequestDto;
import com.example.dongnemashilbe.review.responsedto.DetailPageResponseDto;
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
        if(id == null){
            throw new CustomException(ErrorCode.REVIEW_NOT_EXIST);
        }

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));

        if (!review.getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        review.update(detailPageRequestDto);

        return new DetailPageResponseDto(review);
    }
}


package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.dto.*;
import com.example.dongnemashilbe.review.entity.Like;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.repository.LikeRepository;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;


    public Slice<MainPageReviewResponseDto> findAllByType(String type, Pageable pageable) {
        List<MainPageReviewResponseDto> dtos = new ArrayList<>();
        Slice<Review> reviews;

        if ("likes".equals(type)) {
            reviews = reviewRepository.findAllByLikes(pageable);
        } else if ("recent".equals(type)) {
            reviews = reviewRepository.findAllByRecent(pageable);
        } else {
            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }

        for (Review review : reviews) {
            Integer likeCount = likeRepository.countByReview(review);
            dtos.add(new MainPageReviewResponseDto(review, likeCount));
        }

        return new SliceImpl<>(dtos, pageable, reviews.hasNext());
    }



    public DetailPageResponseDto getReview(Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));
        Integer likeCount = likeRepository.countByReview(review);

        return new DetailPageResponseDto(review, likeCount, reviewRepository.countCommentsByReviewId(id));
    }


    public WriteReviewResponseDto createReview(WriteReviewRequestDto writeReviewRequestDto, User user) {


        if (writeReviewRequestDto.getVideoUrl() == null && writeReviewRequestDto.getImgUrl() == null){

            throw new CustomException(ErrorCode.ELEMENTS_IS_REQUIRED);
        }
        User usercheck = userRepository.findByNickname(user.getNickname())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Review review = new Review(writeReviewRequestDto,usercheck);

        return new WriteReviewResponseDto(reviewRepository.save(review));
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

    @Transactional
    public void deleteReview(Long id, UserDetailsImpl userDetails) {

        Review review = findReviewById(id);
        validate(review, userDetails);
        reviewRepository.delete(review);
    }

    @Transactional
    public LikeResponseDto like(Long review_id, String nickname) {
        Review review = reviewRepository.findById(review_id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Optional<Like> existingLike = likeRepository.findByUserAndReview(user, review);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like newLike = new Like(user, review);
            likeRepository.save(new Like(user, review));
            return new LikeResponseDto(newLike);
        }
        return null;
    }

    public Integer getLikeCount(Long review_id) {
        Review review = reviewRepository.findById(review_id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));
        return likeRepository.countByReview(review);
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




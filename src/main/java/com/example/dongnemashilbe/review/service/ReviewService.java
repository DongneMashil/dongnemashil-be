package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.dto.*;
import com.example.dongnemashilbe.review.entity.Like;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.entity.Review_Tag;
import com.example.dongnemashilbe.review.entity.Tag;
import com.example.dongnemashilbe.review.repository.LikeRepository;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.review.repository.Review_TagRepository;
import com.example.dongnemashilbe.review.repository.TagRepository;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.SuccessMessageDto;
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
    private final TagRepository tagRepository;
    private final Review_TagRepository review_tagRepository;

    public Slice<MainPageReviewResponseDto> findAllByType(String type, Pageable pageable,User user) {
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
            boolean likebool = false;
            if (user != null) {
                likebool = likeRepository.findByUserAndReview(user, review).isPresent();
            }
            dtos.add(new MainPageReviewResponseDto(review, likeCount, likebool));
        }

        return new SliceImpl<>(dtos, pageable, reviews.hasNext());
    }



    public DetailPageResponseDto getReview(Long id,User user) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));
        Integer likeCount = likeRepository.countByReview(review);
        if (user != null) {
            boolean likebool = likeRepository.findByUserAndReview(user, review).isPresent();
            return new DetailPageResponseDto(review, likeCount, reviewRepository.countCommentsByReviewId(id),likebool);
        }
        return new DetailPageResponseDto(review, likeCount, reviewRepository.countCommentsByReviewId(id));
    }


    public WriteReviewResponseDto createReview(WriteReviewRequestDto writeReviewRequestDto, User user) {


        if (writeReviewRequestDto.getVideoUrl() == null && writeReviewRequestDto.getImgUrl() == null){

            throw new CustomException(ErrorCode.ELEMENTS_IS_REQUIRED);
        }
        User usercheck = userRepository.findByNickname(user.getNickname())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Review review = new Review(writeReviewRequestDto,usercheck);
        review = reviewRepository.save(review);

        List<String> tagName = writeReviewRequestDto.getTag();

        for (String tagCategory : tagName) {
            Optional<Tag> existingTag = tagRepository.findByName(tagCategory);

            Tag tag;
            if (existingTag.isPresent()){
                tag = existingTag.get();
            } else {
                tag = new Tag(tagCategory);
                tagRepository.save(tag);
            }
            Review_Tag review_tag = new Review_Tag(review,tag);
            review_tagRepository.save(review_tag);
        }
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
    public SuccessMessageDto like(Long review_id, String nickname) {
        Review review = reviewRepository.findById(review_id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Optional<Like> existingLike = likeRepository.findByUserAndReview(user, review);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return new SuccessMessageDto("좋아요 취소 완료");
        } else {
            likeRepository.save(new Like(user, review));
            return new SuccessMessageDto("좋아요 완료");
        }

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




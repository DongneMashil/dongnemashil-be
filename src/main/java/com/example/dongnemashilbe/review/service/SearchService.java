package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.like.repository.LikeRepository;
import com.example.dongnemashilbe.review.dto.SearchResponseDto;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public Page<SearchResponseDto> search(String type, Integer page, String q, String tag, User user) {

        Pageable pageable = PageRequest.of(page-1, 12);

        List<String> tags = tag != null && !tag.isEmpty() ? tag.contains(",")
                ? Arrays.asList(tag.split(",")) : Collections.singletonList(tag) : Collections.emptyList();
        Page<Review> reviews;

        if ("likes".equals(type)) {
            reviews = tags.isEmpty() ? reviewRepository.findAllByLikesAndAddressContaining(pageable, q)
                    : reviewRepository.findAllByLikesAndTagAndAddressContaining(pageable, tags, q, (long) tags.size());
        } else if ("recent".equals(type)) {
            reviews = tags.isEmpty() ? reviewRepository.findAllByRecentAndAddressContaining(pageable, q)
                    : reviewRepository.findAllByRecentAndTagAndAddressContaining(pageable, tags, q);
        } else {
            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }

        List<SearchResponseDto> dtos = reviews.stream()
                .map(review -> new SearchResponseDto(review, likeRepository.countByReview(review),
                        user != null && likeRepository.findByUserAndReview(user, review).isPresent()))
                .collect(Collectors.toList());
        Page<SearchResponseDto> resultPage = new PageImpl<>(dtos, pageable, reviews.getTotalElements());

        return resultPage;
    }
}

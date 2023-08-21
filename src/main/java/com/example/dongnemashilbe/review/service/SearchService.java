package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.dto.SearchResponseDto;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.like.repository.LikeRepository;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final RedisTemplate<String,String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public Page<SearchResponseDto> search(String type, Integer page, String q, String tag, User user) throws JsonProcessingException {
        String cacheKey = generateCacheKey(type, page, q, tag);

        Object cachedResult = redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            return objectMapper.readValue((String) cachedResult, new TypeReference<PageImpl<SearchResponseDto>>() {});
        }
        Pageable pageable = PageRequest.of(page-1, 12);

        List<String> tags = new ArrayList<>();
        List<SearchResponseDto> dtos = new ArrayList<>();
        Page<Review> reviews;

        if ("likes".equals(type)) {
            if (tag != null){
                if (tag.contains(",")){
                    tags = Arrays.asList(tag.split(","));
                }
                else{
                    tags.add(tag);
                }
            }

            if (!tags.isEmpty()) {
                reviews = reviewRepository.findAllByLikesAndTagAndAddressContaining(pageable,tags,q, (long) tags.size());
            } else {
                reviews = reviewRepository.findAllByLikesAndAddressContaining(pageable,q);
            }
        } else if ("recent".equals(type)) {


            if (tags != null) {
                reviews = reviewRepository.findAllByRecentAndTagAndAddressContaining(pageable ,tags,q);
            } else {
                reviews = reviewRepository.findAllByRecentAndAddressContaining(pageable,q);
            }
        } else {
            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }

        for (Review review : reviews) {
            Integer likeCount = likeRepository.countByReview(review);

            boolean likebool = false;
            if (user != null) {
                likebool = likeRepository.findByUserAndReview(user, review).isPresent();
            }

            String mainImgUrl = review.getMainImgUrl();

            dtos.add(new SearchResponseDto(review, likeCount, mainImgUrl, likebool));
        }

        Page<SearchResponseDto> resultPage = new PageImpl<>(dtos, pageable, reviews.getTotalElements());
        redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(resultPage));

        return resultPage;

    }
    private String generateCacheKey(String type, Integer page, String q, String tag) {
        return "search:" + type + ":" + page + ":" + q + ":" + tag;
    }
}

package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.like.repository.LikeRepository;
import com.example.dongnemashilbe.review.dto.SearchResponseDto;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public Page<SearchResponseDto> search(String type, Integer page, String q, String tag, User user) throws JsonProcessingException {
        String redisKey = buildRedisKey(type, q, tag, page);


        String cachedResult = redisTemplate.opsForValue().get(redisKey); // 레디스에서 결과 가져오기

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());

        if (cachedResult != null) {
            List<SearchResponseDto> dtos = objectMapper.readValue(cachedResult, new TypeReference<List<SearchResponseDto>>() {});
            return new PageImpl<>(dtos, PageRequest.of(page - 1, 12), dtos.size());
        }

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
                .map(review -> new SearchResponseDto(review, likeRepository.countByReview(review), review.getMainImgUrl(),
                        user != null && likeRepository.findByUserAndReview(user, review).isPresent()))
                .collect(Collectors.toList());
        Page<SearchResponseDto> resultPage = new PageImpl<>(dtos, pageable, reviews.getTotalElements());


        // 결과를 레디스에 저장
        redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(dtos));

        Boolean success = redisTemplate.expire(redisKey, 3, TimeUnit.MINUTES);
        if (!success) {

            // 재시도 로직 (예: 최대 3번 재시도)
            int retries = 0;
            while (!success && retries < 3) {
                success = redisTemplate.expire(redisKey, 3, TimeUnit.MINUTES);
                retries++;
            }

            // 여전히 실패한다면 예외 처리
            if (!success) {
                throw new CustomException(ErrorCode.OUT_OF_RANGE);
            }
        }

        return resultPage;
    }
    private String buildRedisKey(String type, String q, String tag,Integer page) {
        return "search:{\"type\":\"" + type + "\",\"query\":\"" + q + "\",\"tag\":\"" + tag + "\",\"page\":\"" + page + "\"}";
    }


}

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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public Page<SearchResponseDto> search(String type, Integer page, String q, String tag, User user) throws JsonProcessingException {
        String redisKey = buildRedisKey(type, q, tag);


        String cachedResult = redisTemplate.opsForValue().get(redisKey); // 레디스에서 결과 가져오기

        ObjectMapper objectMapper = new ObjectMapper();


        if (cachedResult != null) {
            List<SearchResponseDto> dtos = objectMapper.readValue(cachedResult, new TypeReference<List<SearchResponseDto>>() {});
            return new PageImpl<>(dtos, PageRequest.of(page - 1, 12), dtos.size());
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


        // 결과를 레디스에 저장
        redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(dtos));
//        redisTemplate.expire(redisKey, 3, TimeUnit.MINUTES);

        Boolean success = redisTemplate.expire(redisKey, 3, TimeUnit.MINUTES);
        if (!success) {
            log.info("실패냐: " + redisKey);

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
    private String buildRedisKey(String type, String q, String tag) {
        return "search:{\"type\":\"" + type + "\",\"query\":\"" + q + "\",\"tag\":\"" + tag + "\"}";
    }


}

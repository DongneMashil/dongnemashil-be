package com.example.dongnemashilbe.like.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import com.example.dongnemashilbe.like.entity.Like;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.like.repository.LikeRepository;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
//    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public SuccessMessageDto like(Long review_id, String nickname) {
//        String cacheKey = "review:" + review_id + ":likes";

        Review review = reviewRepository.findById(review_id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Optional<Like> existingLike = likeRepository.findByUserAndReview(user, review);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
//            redisTemplate.opsForValue().decrement(cacheKey);
            return new SuccessMessageDto("좋아요 취소 완료");
        } else {
            likeRepository.save(new Like(user, review));
//            redisTemplate.opsForValue().increment(cacheKey);
            return new SuccessMessageDto("좋아요 완료");
        }

    }
}

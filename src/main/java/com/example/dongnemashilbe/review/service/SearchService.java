package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.dto.SearchResponseDto;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.like.repository.LikeRepository;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;

    public Page<SearchResponseDto> search(String type, Integer page, String q, String tag, User user) {
        Pageable pageable = PageRequest.of(page-1, 12);
//        reviewRepository.findByAddress(q);

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
                String qwe = "서울시 "+q+" 나머지 주소 정보";
                reviews = reviewRepository.findAllByLikesAndAddressContaining(pageable,qwe);
            }
        } else if ("recent".equals(type)) {


            if (tags != null) {
                reviews = reviewRepository.findAllByRecentAndTagAndAddressContaining(pageable ,tags,"서울시 "+q);
            } else {
                reviews = reviewRepository.findAllByRecentAndAddressContaining(pageable,"서울시 "+q);
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


        return new PageImpl<SearchResponseDto>(dtos, pageable, reviews.getTotalElements());
    }
}

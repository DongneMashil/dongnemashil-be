package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.like.repository.LikeRepository;
import com.example.dongnemashilbe.review.dto.*;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.entity.Review_Tag;
import com.example.dongnemashilbe.review.entity.Tag;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.review.repository.Review_TagRepository;
import com.example.dongnemashilbe.review.repository.TagRepository;
import com.example.dongnemashilbe.s3.S3Upload;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final TagRepository tagRepository;
    private final Review_TagRepository review_tagRepository;
    private final S3Upload s3Upload;

    public Slice<MainPageReviewResponseDto> findAllByType(String type, Pageable pageable,String tag,User user) {
        List<String> tags = null;
        if (tag != null){
            tags = Arrays.asList(tag.split(","));
        }
        List<MainPageReviewResponseDto> dtos = new ArrayList<>();
        Slice<Review> reviews;
        if ("likes".equals(type)) {
            if (tags != null) {
                reviews = reviewRepository.findAllByLikesAndTags(pageable, tags);
            } else {
                reviews = reviewRepository.findAllByLikes(pageable);
            }
        } else if ("recent".equals(type)) {
            if (tags != null) {
                reviews = reviewRepository.findAllByRecentAndTags(pageable, tags);
            } else {
                reviews = reviewRepository.findAllByRecent(pageable);
            }
        } else {
            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }

        for (Review review : reviews) {
            Integer likeCount = reviewRepository.countLikesForReviewId(review.getId());

            boolean likebool = false;
            if (user != null) {
                likebool = likeRepository.findByUserAndReview(user, review).isPresent();
            }

            String mainImgUrl = review.getMainImgUrl();

            dtos.add(new MainPageReviewResponseDto(review, likeCount, mainImgUrl, likebool));
        }
        return new SliceImpl<>(dtos, pageable, reviews.hasNext());
    }

    public DetailPageResponseDto getReview(Long id, User user) {

        Review review = findReviewById(id);
        Integer likeCount = reviewRepository.countLikesForReviewId(id);
        Long commentCount = reviewRepository.countCommentsByReviewId(id);
        String mainImgUrl = review.getMainImgUrl();
        String subImgUrl = review.getSubImgUrl();
        String videoUrl = review.getVideoUrl();

        boolean likebool = false;
        if (user != null) {
            likebool = likeRepository.findByUserAndReview(user, review).isPresent();
        }

        return new DetailPageResponseDto(review, likeCount, commentCount, mainImgUrl, subImgUrl, videoUrl,likebool);
    }

    public WriteReviewResponseDto createReview(WriteReviewRequestDto writeReviewRequestDto,
                                               User user,
                                               MultipartFile mainImgFile, List<MultipartFile> subImgUrl,
                                               MultipartFile videoFile) throws IOException {

        if ((mainImgFile == null || mainImgFile.isEmpty())) {
            throw new CustomException(ErrorCode.ELEMENTS_IS_REQUIRED);
        }

        validateMediaFiles(mainImgFile, videoFile, subImgUrl);

        MediaUrlsDto mediaUrlsDto = setS3Upload(mainImgFile, subImgUrl, videoFile);

        // 리뷰 등록
        Review review = new Review(writeReviewRequestDto, user,
                mediaUrlsDto.getMainImgUrl(),
                mediaUrlsDto.getSubImageUrlsString(),
                mediaUrlsDto.getVideoUrl());

        review = reviewRepository.save(review);


        // 태그 등록
        List<String> tagName = writeReviewRequestDto.getTag();

        for (String tagCategory : tagName) {
            Optional<Tag> existingTag = tagRepository.findByName(tagCategory);

            Tag tag;
            if (existingTag.isPresent()) {
                tag = existingTag.get();
            } else {
                tag = new Tag(tagCategory);
                tagRepository.save(tag);
            }
            Review_Tag review_tag = new Review_Tag(review, tag);
            review_tagRepository.save(review_tag);
        }
        return new WriteReviewResponseDto(reviewRepository.save(review).getId());
    }

    @Transactional
    public WriteReviewResponseDto updateReview(Long id,
                                               DetailPageRequestDto detailPageRequestDto,
                                               MultipartFile mainImgFile, List<MultipartFile> subImgUrl,
                                               MultipartFile videoFile, UserDetailsImpl userDetails) throws IOException{

        Review review = findReviewById(id);
        validate(review, userDetails);

        validateMediaFiles(mainImgFile, videoFile, subImgUrl);

        MediaUrlsDto mediaUrlsDto = setS3Upload(mainImgFile, subImgUrl, videoFile);

        // 기존 파일 삭제
        if (review.getMainImgUrl() != null) {
            s3Upload.deleteExistingFile(review.getMainImgUrl());
        }
        if (review.getSubImgUrl() != null) {
            for (String subImageUrl : review.getSubImgUrl().split(",")) {
                s3Upload.deleteExistingFile(subImageUrl);
            }
        }
        if (review.getVideoUrl() != null) {
            s3Upload.deleteExistingFile(review.getVideoUrl());
        }

        review.update(detailPageRequestDto, userDetails.getUser(),
                mediaUrlsDto.getMainImgUrl(),
                mediaUrlsDto.getSubImageUrlsString(),
                mediaUrlsDto.getVideoUrl());


        List<Review_Tag> existingTags = review_tagRepository.findAllByReview(review);
        review_tagRepository.deleteAll(existingTags);


        List<String> newTagNames = detailPageRequestDto.getTag();
        for (String newTagName : newTagNames) {
            Tag tag = tagRepository.findByName(newTagName).orElse(new Tag(newTagName));
            review_tagRepository.save(new Review_Tag(review, tag));
        }

        return new WriteReviewResponseDto(review.getId());

    }

    @Transactional
    public void deleteReview(Long id, UserDetailsImpl userDetails) {

        Review review = findReviewById(id);
        validate(review, userDetails);

        if (s3Upload == null) {
            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }
        try {
            if (review.getMainImgUrl() != null) {
                s3Upload.delete(review.getMainImgUrl());
            }
            if (review.getVideoUrl() != null) {
                s3Upload.delete(review.getVideoUrl());
            }
            if (review.getSubImgUrl() != null) {
                for (String subImgUrl : review.getSubImgUrl().split(",")) {
                    if (subImgUrl != null && !subImgUrl.isEmpty()) {
                        s3Upload.delete(review.getSubImgUrl());
                    }
                }
            }
        } catch (Exception e) {

            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }

        reviewRepository.delete(review);
    }
    /*=============================================메서드=============================================================*/
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

    private void validateMediaFiles(MultipartFile mainImgFile, MultipartFile videoFile, List<MultipartFile> subImgUrl){

        List<String> allowedImageExtensions = Arrays.asList("jpg", "jpeg", "png");
        List<String> allowedVideoExtensions = Arrays.asList("mp4","mov");

        if (mainImgFile != null) {
            String mainImgExtension = mainImgFile.getOriginalFilename()
                    .substring(mainImgFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if (!allowedImageExtensions.contains(mainImgExtension)) {
                throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_type);
            }
        }
        // 동영상 파일 검사
        if (videoFile != null) {
            String videoExtension = videoFile.getOriginalFilename()
                    .substring(videoFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if (!allowedVideoExtensions.contains(videoExtension)) {
                throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_type);
            }
        }

        // 서브 이미지 파일 검사
        if (subImgUrl != null && !subImgUrl.isEmpty()) {
            for (MultipartFile file : subImgUrl) {
                String subImgExtension = file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
                System.out.println(file.getOriginalFilename());
                if (!allowedImageExtensions.contains(subImgExtension)) {
                    throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_type);
                }
            }
        }
    }

    private MediaUrlsDto setS3Upload(MultipartFile mainImgFile,
                             List<MultipartFile>subImgUrl,
                             MultipartFile videoFile) throws IOException {

        String mainImgUrl = null;
        if (mainImgFile != null) {
            mainImgUrl = s3Upload.upload(mainImgFile);
        }
        List<String> subImageUrlsList = new ArrayList<>();
        if (subImgUrl != null && !subImgUrl.isEmpty()) {
            for (MultipartFile file : subImgUrl) {
                String imageUrl = s3Upload.upload(file);
                subImageUrlsList.add(imageUrl);
            }
        }
        // subImageUrlsList를 쉼표로 구분된 문자열로 변환
        String subImageUrlsString = String.join(",", subImageUrlsList);

        // 동영상 파일 처리
        String videoUrl = null;
        if (videoFile != null) {
            videoUrl = s3Upload.upload(videoFile);
        }
        return new MediaUrlsDto(mainImgUrl, subImageUrlsString, videoUrl);
    }
}




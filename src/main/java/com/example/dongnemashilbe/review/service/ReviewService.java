package com.example.dongnemashilbe.review.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.dto.*;
import com.example.dongnemashilbe.review.entity.*;
import com.example.dongnemashilbe.review.repository.*;
import com.example.dongnemashilbe.s3.S3Upload;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final TagRepository tagRepository;
    private final Review_TagRepository review_tagRepository;
    private final MediaFileRepository mediaFileRepository;
    private final S3Upload s3Upload;

    public Slice<MainPageReviewResponseDto> findAllByType(String type, Pageable pageable, User user) {
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

            String mainImgUrl = null;
            String videoUrl = null;

            if (!review.getMediaFiles().isEmpty()) {
                MediaFile mediaFile = review.getMediaFiles().get(0);

                // mainImg 있을 시
                if (mediaFile.getMainImgUrl() != null) {
                    mainImgUrl = mediaFile.getMainImgUrl();
                } else if (mediaFile.getVideoUrl() != null) {
                    videoUrl = mediaFile.getVideoUrl();
                }
            }

            dtos.add(new MainPageReviewResponseDto(review, likeCount, mainImgUrl, videoUrl, likebool));

        }
        return new SliceImpl<>(dtos, pageable, reviews.hasNext());
    }

    public DetailPageResponseDto getReview(Long id, User user) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));
        Integer likeCount = likeRepository.countByReview(review);

        MediaFile mediaFile = mediaFileRepository.findByReviewId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ELEMENTS_IS_REQUIRED));
        String mainImgUrl = mediaFile.getMainImgUrl();
        String subImgUrl = mediaFile.getSubImgUrl();
        String videoUrl = mediaFile.getVideoUrl();

        if (user != null) {
            boolean likebool = likeRepository.findByUserAndReview(user, review).isPresent();
            return new DetailPageResponseDto(review, likeCount, reviewRepository.countCommentsByReviewId(id), mainImgUrl, subImgUrl, videoUrl,likebool);
        }

        return new DetailPageResponseDto(review, likeCount, reviewRepository.countCommentsByReviewId(id),
                mainImgUrl, subImgUrl, videoUrl);
    }

    public WriteReviewResponseDto createReview(WriteReviewRequestDto writeReviewRequestDto,
                                               User user,
                                               MultipartFile mainImgFile, List<MultipartFile> subImgUrl,
                                               MultipartFile videoFile) throws IOException {

        if ((mainImgFile == null || mainImgFile.isEmpty()) && (videoFile == null || videoFile.isEmpty())) {
            throw new CustomException(ErrorCode.ELEMENTS_IS_REQUIRED);

        }
        List<String> allowedImageExtensions = Arrays.asList("jpg", "jpeg", "png");
        List<String> allowedVideoExtensions = Arrays.asList("mp4");

        if (mainImgFile != null) {
            String mainImgExtension = mainImgFile.getOriginalFilename()
                    .substring(mainImgFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            System.out.println(mainImgExtension);

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

        // 이미지 파일 처리
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

        // 동영상 파일 처리
        String videoUrl = null;
        if (videoFile != null) {
            videoUrl = s3Upload.upload(videoFile);
        }

        User usercheck = userRepository.findByNickname(user.getNickname())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 리뷰 등록
        Review review = new Review(writeReviewRequestDto, usercheck);
        review = reviewRepository.save(review);

        // subImageUrlsList를 쉼표로 구분된 문자열로 변환
        String subImageUrlsString = String.join(",", subImageUrlsList);

        // 미디어 파일 생성 및 저장
        MediaFile mediaFile = new MediaFile(mainImgUrl, subImageUrlsString, videoUrl, review);

        mediaFileRepository.save(mediaFile);


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
                                               MultipartFile videoFile, UserDetailsImpl userDetails) throws IOException {
        Review review = findReviewById(id);
        validate(review, userDetails);
        review.update(detailPageRequestDto);
        // 이미지 영상 타입 체크
        List<String> allowedImageExtensions = Arrays.asList("jpg", "jpeg", "png");
        List<String> allowedVideoExtensions = Arrays.asList("mp4");

        if (mainImgFile != null) {
            String mainImgExtension = mainImgFile.getOriginalFilename().substring(mainImgFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if (!allowedImageExtensions.contains(mainImgExtension)) {
                throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_type);

            }
        }
        // 동영상 파일 검사
        if (videoFile != null) {
            String videoExtension = videoFile.getOriginalFilename().substring(videoFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if (!allowedVideoExtensions.contains(videoExtension)) {
                throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_type);
            }
        }

        // 서브 이미지 파일 검사
        if (subImgUrl != null && !subImgUrl.isEmpty()) {
            for (MultipartFile file : subImgUrl) {
                String subImgExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
                if (!allowedImageExtensions.contains(subImgExtension)) {
                    throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_type);
                }
            }
        }
        // 이미지 파일 처리
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
        String subImageUrlsString = String.join(",", subImageUrlsList);

        // 비디오 파일 처리
        String videoUrl = null;
        if (videoFile != null) {
            videoUrl = s3Upload.upload(videoFile);
        }

        // 리뷰 업데이트
        review.update(detailPageRequestDto);

        // 미디어 파일 디비 업데이트
        MediaFile mediaFile = mediaFileRepository.findByReview(review)
                .orElse(new MediaFile(mainImgUrl, subImageUrlsString, videoUrl, review));
        mediaFile.update(mainImgUrl, subImageUrlsString, videoUrl);
        mediaFileRepository.save(mediaFile);

        // 태그 업데이트
        List<String> tagName = detailPageRequestDto.getTag();
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

        return new WriteReviewResponseDto(review.getId());
    }

    @Transactional
    public void deleteReview(Long id, UserDetailsImpl userDetails) {

        Review review = findReviewById(id);
        validate(review, userDetails);

        MediaFile mediaFile = mediaFileRepository.findByReview(review)
                .orElseThrow(() -> new CustomException(ErrorCode.ELEMENTS_IS_REQUIRED));
        if (mediaFile.getMainImgUrl() != null) {
            s3Upload.delete(mediaFile.getMainImgUrl());
        }
        if (mediaFile.getVideoUrl() != null) {
            s3Upload.delete(mediaFile.getVideoUrl());
        }
        if (mediaFile.getSubImgUrl() != null) {
            for (String subImgUrl : mediaFile.getSubImgUrl().split(",")) {
                s3Upload.delete(subImgUrl);
            }
        }

        mediaFileRepository.delete(mediaFile);

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




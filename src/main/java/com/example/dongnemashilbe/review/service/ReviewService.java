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
import com.example.dongnemashilbe.user.dto.MyPageListResponseDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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
    private final UserRepository userRepository;

    public Slice<MainPageReviewResponseDto> getAllReviews(String type,Integer page, Integer size,String tag,User user) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<String> tags = null;
        if (tag != null){
            tags = Arrays.asList(tag.split(","));
        }
        List<MainPageReviewResponseDto> dtos = new ArrayList<>();
        Slice<Review> reviews;
        if ("likes".equals(type)) {
            if (tags != null) {
                reviews = reviewRepository.findAllByLikesAndTags(pageable, tags,(long) tags.size());
            } else {
                reviews = reviewRepository.findAllByLikes(pageable);
            }
        } else if ("recent".equals(type)) {
            if (tags != null) {
                reviews = reviewRepository.findAllByRecentAndTags(pageable, tags,(long) tags.size());
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
            dtos.add(new MainPageReviewResponseDto(review, likeCount, likebool));
        }
        return new SliceImpl<>(dtos, pageable, reviews.hasNext());
    }

    // 단일 리뷰 조회
    public DetailPageResponseDto getReview(Long id, User user) {

        Review review = findReviewById(id);
        Integer likeCount = reviewRepository.countLikesForReviewId(id);
        Long commentCount = reviewRepository.countCommentsByReviewId(id);


        boolean likebool = false;
        if (user != null) {
            likebool = likeRepository.findByUserAndReview(user, review).isPresent();
        }

        return new DetailPageResponseDto(review, likeCount, commentCount,likebool);
    }

    // 리뷰 작성
    public WriteReviewResponseDto createReview(WriteReviewRequestDto writeReviewRequestDto,
                                               User user,
                                               MultipartFile mainImgFile, List<MultipartFile> subImgUrl,
                                               MultipartFile videoFile) throws IOException {

        if ((mainImgFile == null || mainImgFile.isEmpty())) {
            throw new CustomException(ErrorCode.ELEMENTS_IS_REQUIRED);
        }

        validateMediaFiles(mainImgFile, videoFile, subImgUrl);

        String smallMainImg = resizingS3Upload(mainImgFile,360);
        String middleMainImg = resizingS3Upload(mainImgFile,768);

        MediaUrlsDto mediaUrlsDto = setS3Upload(mainImgFile, subImgUrl, videoFile);

        // 리뷰 등록
        Review review = new Review(writeReviewRequestDto, user,
                mediaUrlsDto.getMainImgUrl(),
                mediaUrlsDto.getSubImageUrlsString(),
                mediaUrlsDto.getVideoUrl(),smallMainImg,middleMainImg,
                mediaUrlsDto.getMiddleSubImageUrlsString(),mediaUrlsDto.getSmallSubImageUrlsString());

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

    //리뷰 수정
    @Transactional
    public WriteReviewResponseDto updateReview(Long id,
                                               DetailPageRequestDto detailPageRequestDto,
                                               MultipartFile mainImgFile, List<MultipartFile> subImgUrl,
                                               MultipartFile videoFile, UserDetailsImpl userDetails) throws IOException{

        Review review = findReviewById(id);
        validate(review, userDetails);

        validateMediaFiles(mainImgFile, videoFile, subImgUrl);

        MediaUrlsDto mediaUrlsDto = setS3Upload(mainImgFile, subImgUrl, videoFile);
        String smallMainImg = resizingS3Upload(mainImgFile,360);
        String middleMainImg = resizingS3Upload(mainImgFile,768);
        // 기존 파일 삭제
        deleteS3(review,s3Upload);

        review.update(detailPageRequestDto, userDetails.getUser(),
                mediaUrlsDto.getMainImgUrl(),
                mediaUrlsDto.getSubImageUrlsString(),
                mediaUrlsDto.getVideoUrl(),
                mediaUrlsDto.getMiddleSubImageUrlsString(),
                mediaUrlsDto.getSmallSubImageUrlsString(),
                smallMainImg,middleMainImg);


        List<Review_Tag> existingTags = review_tagRepository.findAllByReview(review);
        review_tagRepository.deleteAll(existingTags);

        //태그 등록
        List<String> newTagNames = detailPageRequestDto.getTag();
        for (String newTagName : newTagNames) {
            Tag tag = tagRepository.findByName(newTagName).orElse(new Tag(newTagName));
            review_tagRepository.save(new Review_Tag(review, tag));
        }

        return new WriteReviewResponseDto(review.getId());

    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Long id, UserDetailsImpl userDetails) {

        Review review = findReviewById(id);
        validate(review, userDetails);

        try {
            deleteS3(review,s3Upload);
        } catch (Exception e) {

            throw new CustomException(ErrorCode.OUT_OF_RANGE);
        }

        reviewRepository.delete(review);
    }

    //유저의 리뷰 리스트 조회
    public Slice<MyPageListResponseDto> getUserReview(String nickname,Integer page,Integer size) {
        User user = userRepository.findByNickname(nickname).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(page - 1, size);
        Slice<Review> reviews = reviewRepository.findAllByUser_Id(user.getId(),pageable);
        List<MyPageListResponseDto> reviewDtos = reviews.getContent().stream()
                .map(MyPageListResponseDto::new)
                .toList();

        return new SliceImpl<>(reviewDtos,pageable,reviews.hasNext() );
    }

    //유저의 프로필이미지 조회
    public String getUserImg(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return user.getProfileImgUrl();
    }

    /*=============================================메서드=============================================================*/

    //리뷰 조회
    private Review findReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));
    }

    // 작성자 확인 메서드
    private void validate(Review review, UserDetailsImpl userDetails) {

        if (!userDetails.getUser().getRole().getAuthority().equals("ROLE_ADMIN")) {
            if (!review.getUser().getId().equals(userDetails.getUser().getId())) {
                throw new CustomException(ErrorCode.NOT_THE_AUTHOR);
            }
        }
    }

    // 확장자명 판별 메서드
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

    //S3이미지 업로드
    private MediaUrlsDto setS3Upload(MultipartFile mainImgFile,
                                     List<MultipartFile>subImgUrl,
                                     MultipartFile videoFile) throws IOException {

        String mainImgUrl = null;
        if (mainImgFile != null) {
            mainImgUrl = s3Upload.upload(mainImgFile);
        }
        List<String> subImageUrlsList = new ArrayList<>();
        List<String> middleSubImageUrlsList = new ArrayList<>();
        List<String> smallSubImageUrlsList = new ArrayList<>();
        if (subImgUrl != null && !subImgUrl.isEmpty()) {
            for (MultipartFile file : subImgUrl) {
                String imageUrl = s3Upload.upload(file);
                String smallSubImg = resizingS3Upload(file,360);
                String middleSubImg = resizingS3Upload(file,768);
                subImageUrlsList.add(imageUrl);
                smallSubImageUrlsList.add(smallSubImg);
                middleSubImageUrlsList.add(middleSubImg);
            }
        }
        // subImageUrlsList를 쉼표로 구분된 문자열로 변환
        String subImageUrlsString = String.join(",", subImageUrlsList);
        String middleSubImageUrlsString = String.join(",", middleSubImageUrlsList);
        String smallSubImageUrlsString = String.join(",", smallSubImageUrlsList);
        // 동영상 파일 처리
        String videoUrl = null;
        if (videoFile != null) {
            videoUrl = s3Upload.upload(videoFile);
        }
        return new MediaUrlsDto(mainImgUrl,subImageUrlsString,
                middleSubImageUrlsString,smallSubImageUrlsString,videoUrl);
    }

    //이미지 리사이징
    private String resizingS3Upload(MultipartFile mainImgFile,int width) throws IOException {
        //360x480
        BufferedImage image = ImageIO.read(mainImgFile.getInputStream());
        int targetHeight = (int) ((double) image.getHeight() / (double) image.getWidth() * width);
        BufferedImage thumbnail = Thumbnails.of(image)
                .size(width, targetHeight)
                .asBufferedImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String mainImgExtension = mainImgFile.getOriginalFilename()
                .substring(mainImgFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if ("png".contains(mainImgExtension)) {
            ImageIO.write(thumbnail, "png", baos);
        }else {
            ImageIO.write(thumbnail, "jpg", baos);
        }


        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        return s3Upload.upload2(mainImgFile.getOriginalFilename(), inputStream);
    }

    // 리뷰 삭제
    private void deleteS3(Review review, S3Upload s3Upload) throws UnsupportedEncodingException {
        if (review.getMainImgUrl() != null) {
            s3Upload.delete(review.getMainImgUrl());
            s3Upload.delete(review.getMiddleMainImgUrl());
            s3Upload.delete(review.getSmallMainImgUrl());
        }
        if (review.getVideoUrl() != null) {
            s3Upload.delete(review.getVideoUrl());
        }
        if (!review.getSubImgUrl().isEmpty()) {
            for (String subImgUrl : review.getSubImgUrl().split(",")) {
                if (subImgUrl != null && !subImgUrl.isEmpty()) {
                    s3Upload.delete(review.getSubImgUrl());
                }
            }
            for (String subImgUrl : review.getMiddleSubImgUrl().split(",")) {
                if (subImgUrl != null && !subImgUrl.isEmpty()) {
                    s3Upload.delete(review.getSubImgUrl());
                }
            }
            for (String subImgUrl : review.getSmallSubImgUrl().split(",")) {
                if (subImgUrl != null && !subImgUrl.isEmpty()) {
                    s3Upload.delete(review.getSubImgUrl());
                }
            }
        }
    }
}

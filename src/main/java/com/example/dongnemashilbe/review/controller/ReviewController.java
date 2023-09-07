package com.example.dongnemashilbe.review.controller;


import com.example.dongnemashilbe.review.dto.*;
import com.example.dongnemashilbe.review.service.ReviewService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.MyPageListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 리스트 조회
    @GetMapping("")
    public Slice<MainPageReviewResponseDto> getAllReviews(
            @RequestParam(value = "type", defaultValue = "likes") String type,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        }catch (NullPointerException e){
            return reviewService.getAllReviews(type, page,size,tag,null);
        }
        return reviewService.getAllReviews(type, page,size,tag,userDetails.getUser());
    }

    //리뷰 단일 조회
    @GetMapping("/{id}")
    public DetailPageResponseDto getReview(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            userDetails.getUser();
        }catch (NullPointerException e){

            return reviewService.getReview(id,null);
        }

        return reviewService.getReview(id,userDetails.getUser());
    }

    //유저가 작성한 리뷰리스트 조회
    @GetMapping("/user")
    public Slice<MyPageListResponseDto> getUserReview(@RequestParam String nickname,
                                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "6") Integer size){
        return reviewService.getUserReview(nickname,page,size);
    }

    //유저의 프로필 이미지 조회
    @GetMapping("/userimg")
    public String getUserImg(@RequestParam String nickname){
        return reviewService.getUserImg(nickname);
    }

    //리뷰 작성
    @PostMapping(value = "",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public WriteReviewResponseDto createReview(@RequestPart (name = "data" )WriteReviewRequestDto writeReviewRequestDto,
                                               @RequestPart (name = "mainImgUrl",required = false) MultipartFile mainImgUrl,
                                               @RequestPart (name = "subImgUrl",required = false) List<MultipartFile> subImgUrl,
                                               @RequestPart (name = "videoUrl",required = false) MultipartFile videoFIle,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails)throws IOException {
        return reviewService.createReview(writeReviewRequestDto, userDetails.getUser(),mainImgUrl,subImgUrl,videoFIle);
    }

    //리뷰 수정
    @PutMapping(value = "/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public WriteReviewResponseDto updateReview(@RequestPart (value = "data") DetailPageRequestDto detailPageRequestDto,
                                               @RequestPart (name = "mainImgUrl",required = false) MultipartFile mainImgUrl,
                                               @RequestPart (name = "subImgUrl",required = false) List<MultipartFile> subImgUrl,
                                               @RequestPart (name = "videoUrl",required = false) MultipartFile videoUrl,
                                               @PathVariable Long id,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return reviewService.updateReview(id,detailPageRequestDto,mainImgUrl,subImgUrl,videoUrl,userDetails);
    }

    //리뷰 삭제
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        reviewService.deleteReview(id, userDetails);
    }

}


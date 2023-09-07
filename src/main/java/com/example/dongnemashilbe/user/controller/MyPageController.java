package com.example.dongnemashilbe.user.controller;

import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.MyCommentResponseDto;
import com.example.dongnemashilbe.user.dto.MyPageListResponseDto;
import com.example.dongnemashilbe.user.dto.MyPageResponseDto;
import com.example.dongnemashilbe.user.dto.ValidateNickname;
import com.example.dongnemashilbe.user.service.MypageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MypageService mypageService;

    // 유저 정보가져오기
    @GetMapping("")
    public MyPageResponseDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getUserInfo(userDetails.getUser());
    }

    // 유저 정보 수정
    @PatchMapping("")
    public SuccessMessageDto modifyUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @Valid @RequestPart (name = "nickname" ) ValidateNickname validateNickname,
                                            @RequestPart(name = "file", required = false) MultipartFile file,
                                            HttpServletResponse response
                                            ) throws IOException {
        return mypageService.modifyUserInfo(userDetails.getUser().getId(), validateNickname.getNickname(),file,response);
    }

    // 유저 좋아요 갯수 가져오기
    @GetMapping("/list")
    public Slice<MyPageListResponseDto> getMyList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @RequestParam(value = "page", defaultValue = "1")  Integer page,
                                                  @RequestParam String q){
        return mypageService.getMyList(page,userDetails.getUser().getId(),q);

    }

    //유저 댓글 갯수 가져오기
    @GetMapping("/comments")
    public Slice<MyCommentResponseDto> getMyCommentList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam(value = "page", defaultValue = "1") Integer page
                                                     ){
        return mypageService.getMyCommentList(userDetails.getUser().getId(),page);
    }
}

package com.example.dongnemashilbe.user.controller;

import com.example.dongnemashilbe.comment.dto.CommentResponseDto;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.MyPageListResponseDto;
import com.example.dongnemashilbe.user.dto.MyPageResponseDto;
import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MypageService mypageService;


    @GetMapping("")
    public MyPageResponseDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getUserInfo(userDetails.getUser());
    }


    @PatchMapping("")
    public SuccessMessageDto modifyUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestPart(name = "nickname") String nickname,
                                            @RequestPart(name = "file", required = false) MultipartFile file
                                            ) throws IOException {
        return mypageService.modifyUserInfo(userDetails.getUser().getId(), nickname,file);
    }

    @GetMapping("/list")
    public Slice<MyPageListResponseDto> getMyList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @RequestParam Integer page,
                                                  @RequestParam String q){
        return mypageService.getMyList(page,userDetails.getUser().getId(),q);

    }


    @GetMapping("/comments")
    public Slice<CommentResponseDto> getMyCommentList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestParam(value = "page", defaultValue = "1") Integer page
                                                     ){
        return mypageService.getMyCommentList(userDetails.getUser().getId(),page);
    }
}

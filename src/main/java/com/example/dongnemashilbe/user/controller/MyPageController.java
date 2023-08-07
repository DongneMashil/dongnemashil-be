package com.example.dongnemashilbe.user.controller;

import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.MyPageResponseDto;
import com.example.dongnemashilbe.user.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

}

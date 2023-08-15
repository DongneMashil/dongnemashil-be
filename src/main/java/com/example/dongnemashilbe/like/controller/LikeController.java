package com.example.dongnemashilbe.like.controller;

import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import com.example.dongnemashilbe.like.service.LikeService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{id}/likes")
    public SuccessMessageDto like(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.like(id, userDetails.getUser().getNickname());
    }
}

package com.example.dongnemashilbe.comment.controller;

import com.example.dongnemashilbe.comment.dto.CommentRequestDto;
import com.example.dongnemashilbe.comment.dto.CommentResponseDto;
import com.example.dongnemashilbe.comment.service.CommentService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.SuccessMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/reviews/{review_id}/comment")
    public SuccessMessageDto writeComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody CommentRequestDto commentRequestDto ,
                                          @PathVariable Long review_id){
        return commentService.writeComment(review_id ,userDetails.getUser() , commentRequestDto);
    }
}

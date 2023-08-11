package com.example.dongnemashilbe.comment.controller;

import com.example.dongnemashilbe.comment.dto.CommentRequestDto;
import com.example.dongnemashilbe.comment.dto.CommentResponseDto;
import com.example.dongnemashilbe.comment.service.CommentService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    @GetMapping("/reviews/{review_id}/comments")
    public Page<CommentResponseDto> getCommentList(@PathVariable Long review_id,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "20") Integer size ){
        return commentService.getCommentList(review_id,page,size);
    }

    @PostMapping("/reviews/{review_id}/comments")
    public SuccessMessageDto writeComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody CommentRequestDto commentRequestDto ,
                                          @PathVariable Long review_id){
        return commentService.writeComment(review_id ,userDetails.getUser() , commentRequestDto);
    }


    @PutMapping("/comments/{comment_id}")
    public SuccessMessageDto updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestBody CommentRequestDto commentRequestDto,
                                           @PathVariable Long comment_id){
        return commentService.updateComment(userDetails.getUser(),commentRequestDto,comment_id);
    }

    @DeleteMapping("/comments/{comment_id}")
    public SuccessMessageDto deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long comment_id){
        return commentService.deleteComment(userDetails.getUser(),comment_id);
    }
}

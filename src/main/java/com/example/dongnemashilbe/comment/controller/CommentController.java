package com.example.dongnemashilbe.comment.controller;

import com.example.dongnemashilbe.comment.dto.CommentRequestDto;
import com.example.dongnemashilbe.comment.dto.CommentResponseDto;
import com.example.dongnemashilbe.comment.service.CommentService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.example.dongnemashilbe.user.dto.SuccessMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    @GetMapping("/reviews/{review_id}/comment")
    public Slice<CommentResponseDto> getCommentList(@PathVariable Long review_id,
                                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "20") Integer size ){
        return commentService.getCommentList(review_id,page,size);
    }

    @PostMapping("/reviews/{review_id}/comment")
    public SuccessMessageDto writeComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody CommentRequestDto commentRequestDto ,
                                          @PathVariable Long review_id){
        return commentService.writeComment(review_id ,userDetails.getUser() , commentRequestDto);
    }
}

package com.example.dongnemashilbe.comment.service;

import com.example.dongnemashilbe.comment.dto.CommentRequestDto;
import com.example.dongnemashilbe.comment.dto.CommentResponseDto;
import com.example.dongnemashilbe.comment.entity.Comment;
import com.example.dongnemashilbe.comment.repository.CommentRepository;
import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.user.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;


    public Slice<CommentResponseDto> getCommentList(Long review_id, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return commentRepository.findAllByReviewId(review_id,pageable).map(CommentResponseDto::new);
    }


    public SuccessMessageDto writeComment(Long review_id, User user, CommentRequestDto commentRequestDto) {
        Review review = reviewRepository.findById(review_id).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));
        Comment comment = new Comment(commentRequestDto.getComment(),review,user);
        commentRepository.save(comment);

        return new SuccessMessageDto("댓글 작성이 완료되었습니다.");
    }


}

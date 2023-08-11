package com.example.dongnemashilbe.comment.service;

import com.example.dongnemashilbe.comment.dto.CommentRequestDto;
import com.example.dongnemashilbe.comment.dto.CommentResponseDto;
import com.example.dongnemashilbe.comment.entity.Comment;
import com.example.dongnemashilbe.comment.repository.CommentRepository;
import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.review.entity.Review;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;


    public Page<CommentResponseDto> getCommentList(Long review_id, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return commentRepository.findAllByReviewId(review_id,pageable).map(CommentResponseDto::new);
    }


    public SuccessMessageDto writeComment(Long review_id, User user, CommentRequestDto commentRequestDto) {
        Review review = reviewRepository.findById(review_id).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_EXIST));
        Comment comment = new Comment(commentRequestDto.getComment(),review,user);
        commentRepository.save(comment);

        return new SuccessMessageDto("댓글 작성이 완료되었습니다.");
    }

    @Transactional
    public SuccessMessageDto updateComment(User user, CommentRequestDto commentRequestDto, Long comment_id) {
     Comment comment = commentRepository.findById(comment_id).orElseThrow(
             ()-> new CustomException(ErrorCode.COMMENT_NOT_EXIST));
     if(!user.getNickname().equals(comment.getUser().getNickname())){
         throw new CustomException(ErrorCode.NOT_THE_AUTHOR);
     }
     comment.update(commentRequestDto.getComment());

        return new SuccessMessageDto("댓글 수정이 완료되었습니다.");
    }

    public SuccessMessageDto deleteComment(User user, Long comment_id) {
        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                ()-> new CustomException(ErrorCode.COMMENT_NOT_EXIST));
        if(!user.getNickname().equals(comment.getUser().getNickname())){
            throw new CustomException(ErrorCode.NOT_THE_AUTHOR);
        }
        commentRepository.delete(comment);

        return new SuccessMessageDto("댓글 삭제가 완료되었습니다.");
    }
}

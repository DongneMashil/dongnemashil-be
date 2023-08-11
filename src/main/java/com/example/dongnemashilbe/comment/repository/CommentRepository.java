package com.example.dongnemashilbe.comment.repository;

import com.example.dongnemashilbe.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findAllByReviewId(Long review_id, Pageable pageable);
    Slice<Comment> findAllByUser_Id(Long user_id, Pageable pageable);
}

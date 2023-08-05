package com.example.dongnemashilbe.comment.repository;

import com.example.dongnemashilbe.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Slice<Comment> findAllByReviewId(Long review_id, Pageable pageable);
}

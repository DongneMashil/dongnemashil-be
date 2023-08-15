package com.example.dongnemashilbe.review.repository;

import com.example.dongnemashilbe.comment.entity.Comment;
import com.example.dongnemashilbe.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //좋아요 카운트
    @Query("SELECT COUNT(l) FROM Like l WHERE l.review.id = :reviewId")
    Integer countLikesForReviewId(@Param("reviewId") Long reviewId);

    // 좋아요 순으로 조회
    @Query("SELECT r FROM Review r ORDER BY SIZE(r.likes) DESC, r.id DESC")
    Slice<Review> findAllByLikes(Pageable pageable);

    //태그 없이 주소를 좋아요 순으로 조회
    @Query("SELECT r FROM Review r WHERE r.address LIKE :address% ORDER BY SIZE(r.likes) DESC")
    Page<Review> findAllByLikesAndAddressContaining(Pageable pageable, @Param("address") String address);

    //태그와 주소를 좋아요 순으로 조회
    @Query("SELECT DISTINCT r FROM Review r " +
            "JOIN r.review_tagList t " +
            "WHERE r.address LIKE %:address% " +
            "AND t.tag.name IN :tagNames " +
            "GROUP BY r " +
            "HAVING COUNT(DISTINCT t.tag) = :tagCount " +
            "ORDER BY SIZE(r.likes) DESC")
    Page<Review> findAllByLikesAndTagAndAddressContaining(
            Pageable pageable,
            @Param("tagNames") List<String> tagNames,
            @Param("address") String address,
            @Param("tagCount") Long tagCount);

    //태그 없이 주소를 최신순으로 조회
    @Query("SELECT r FROM Review r WHERE r.address LIKE %:address% ORDER BY r.createdAt DESC")
    Page<Review> findAllByRecentAndAddressContaining(Pageable pageable, @Param("address") String address);

    //태그와 주소를 최신순으로 조회
    @Query("SELECT DISTINCT r FROM Review r " +
            "JOIN r.review_tagList t WHERE r.address LIKE %:address% " +
            "AND t.tag.name IN :tagNames ORDER BY r.createdAt DESC")
    Page<Review> findAllByRecentAndTagAndAddressContaining(Pageable pageable,@Param("tagNames") List<String> tagNames,
                                                           @Param("address") String address);

    // 최신순으로 조회
    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    Slice<Review> findAllByRecent(Pageable pageable);

    @Query("SELECT COUNT(c) from Comment c where c.review.id = :reviewId")
    Long countCommentsByReviewId(@Param("reviewId") Long reviewId);

    // 태그를 좋아요순으로 조회
    @Query("SELECT rt.review FROM Review_Tag rt " +
            "WHERE rt.tag.name IN :tags GROUP BY rt.review ORDER BY SIZE(rt.review.likes) DESC, rt.review.id DESC")
    Slice<Review> findAllByLikesAndTags(Pageable pageable, @Param("tags") List<String> tags);

    // 태그를 최신순으로 조회
    @Query("SELECT rt.review FROM Review_Tag rt " +
            "WHERE rt.tag.name IN :tags GROUP BY rt.review ORDER BY rt.review.createdAt DESC")
    Slice<Review> findAllByRecentAndTags(Pageable pageable, @Param("tags") List<String> tags);

    // 유저가 작성한 리뷰 조회
    Slice<Review> findAllByUser_Id(Long userId, Pageable pageable);

}


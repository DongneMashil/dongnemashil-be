package com.example.dongnemashilbe.review.repository;

import com.example.dongnemashilbe.review.entity.Review_Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Review_TagRepository extends JpaRepository<Review_Tag,Long>{

}

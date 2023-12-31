package com.example.dongnemashilbe.user.repository;

import com.example.dongnemashilbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByKakaoId(Long kakao);
    Optional<User> findByNickname(String nickname);

}

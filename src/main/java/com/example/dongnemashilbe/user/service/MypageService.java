package com.example.dongnemashilbe.user.service;

import com.example.dongnemashilbe.comment.repository.CommentRepository;
import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.like.repository.LikeRepository;
import com.example.dongnemashilbe.review.repository.ReviewRepository;
import com.example.dongnemashilbe.s3.S3Upload;
import com.example.dongnemashilbe.security.jwt.JwtUtil;
import com.example.dongnemashilbe.user.dto.MyCommentResponseDto;
import com.example.dongnemashilbe.user.dto.MyPageListResponseDto;
import com.example.dongnemashilbe.user.dto.MyPageResponseDto;
import com.example.dongnemashilbe.global.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.entity.UserRoleEnum;
import com.example.dongnemashilbe.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final S3Upload s3Upload;
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    // 유저정보 조회 메서드
    public MyPageResponseDto getUserInfo(User user) {
        return new MyPageResponseDto(user);
    }

    // 유저정보 수정 메서드
    @Transactional
    public SuccessMessageDto modifyUserInfo(Long id, String nickname,
                                            MultipartFile file, HttpServletResponse response) throws IOException {

       User user = userRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));
        if (!user.getNickname().equals(nickname))
            if (userRepository.findByNickname(nickname).isPresent())
                throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);

        if(user.getProfileImgUrl() != null)
            s3Upload.delete(user.getProfileImgUrl());
        String S3Url = s3Upload.upload(file);

       user.uploadUser(nickname,S3Url);

        String accessToken = jwtUtil.createAccessToken(nickname, UserRoleEnum.USER);
        String refreshToken = jwtUtil.createRefreshToken(nickname);

        jwtUtil.addJwtToHeader(JwtUtil.ACCESSTOKEN_HEADER,accessToken,response);
        jwtUtil.addJwtToHeader(JwtUtil.REFRESHTOKEN_HEADER,refreshToken,response);

        return new SuccessMessageDto("회원정보 수정이 완료 되었습니다.");
    }

    //유저 좋아요 갯수 조회 메서드
    @Transactional
    public Page<MyPageListResponseDto> getMyList(Integer page , Long userId, String q) {
        Pageable pageable = PageRequest.of(page - 1, 8);

        if (q.equals("likes")) {
           return likeRepository.findAllByUser_Id(userId,pageable).map(MyPageListResponseDto::new);
        }

        return reviewRepository.findAllByUser_Id(userId,pageable).map(MyPageListResponseDto::new);
    }

    //유저 댓글 갯수 조회 메서드
    public Page<MyCommentResponseDto> getMyCommentList(Long id, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 16);

        return commentRepository.findAllByUser_Id(id,pageable).map(MyCommentResponseDto::new);
    }
}

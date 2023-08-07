package com.example.dongnemashilbe.user.service;

import com.example.dongnemashilbe.exception.CustomException;
import com.example.dongnemashilbe.exception.ErrorCode;
import com.example.dongnemashilbe.s3.S3Upload;
import com.example.dongnemashilbe.user.dto.MyPageResponseDto;
import com.example.dongnemashilbe.user.dto.SuccessMessageDto;
import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final S3Upload s3Upload;


//    String url = s3Upload.upload(file);

    public MyPageResponseDto getUserInfo(User user) {
        System.out.println("user.getId() = " + user.getId());
        User user1 = userRepository.findById(user.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return new MyPageResponseDto(user1);
    }


    @Transactional
    public SuccessMessageDto modifyUserInfo(Long id, String nickname, MultipartFile file) throws IOException {
       if (!userRepository.findByNickname(nickname).isEmpty())
           throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
       String S3Url = s3Upload.upload(file);

       User user = userRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_USER));
       user.uploadUser(nickname,S3Url);


        return new SuccessMessageDto("회원정보 수정이 완료 되었습니다.");
    }
}

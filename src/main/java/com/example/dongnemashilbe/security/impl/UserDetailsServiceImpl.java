package com.example.dongnemashilbe.security.impl;

import com.example.dongnemashilbe.user.entity.User;
import com.example.dongnemashilbe.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String Email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(Email).orElseThrow(() -> new UsernameNotFoundException("등록된 사용자가 아닙니다." +Email));

        return new UserDetailsImpl(user);
    }

    public UserDetails loadUserByNickname(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new UsernameNotFoundException("등록된 사용자가 아닙니다." +nickname));

        return new UserDetailsImpl(user);
    }
}

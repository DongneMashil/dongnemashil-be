package com.example.dongnemashilbe.user;

import com.example.dongnemashilbe.config.WebSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("인증")
@Import(WebSecurityConfig.class)
@WebMvcTest
class UserControllerTest {

    @Test
    void signup() {
    }

    @Test
    void login() {
    }
}
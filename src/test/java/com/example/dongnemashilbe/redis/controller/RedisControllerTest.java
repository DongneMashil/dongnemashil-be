package com.example.dongnemashilbe.redis.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RedisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSetString() throws Exception {
        mockMvc.perform(post("/redis/test/setString")
                        .param("testkey", "key1")
                        .param("testvalue", "value1"))
                .andExpect(status().isOk());
    }

    // 다른 테스트 케이스들도 추가
}

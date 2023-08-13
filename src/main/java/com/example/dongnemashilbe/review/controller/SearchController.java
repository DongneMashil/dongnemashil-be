package com.example.dongnemashilbe.review.controller;

import com.example.dongnemashilbe.aop.ExeTimer;
import com.example.dongnemashilbe.review.dto.SearchResponseDto;
import com.example.dongnemashilbe.review.service.SearchService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @ExeTimer
    @GetMapping("")
    public Page<SearchResponseDto> search(@RequestParam(value = "type", defaultValue = "likes") String type,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(required = false) String tag,
                                          @RequestParam String q,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            userDetails.getUser();
        }catch (NullPointerException e){
            return searchService.search(type,page, q, tag,null);
        }
        return searchService.search(type,page, q, tag,userDetails.getUser());
    }


}

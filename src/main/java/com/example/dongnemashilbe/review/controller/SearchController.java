package com.example.dongnemashilbe.review.controller;

import com.example.dongnemashilbe.aop.ExeTimer;
import com.example.dongnemashilbe.review.dto.RadiusResponseDto;
import com.example.dongnemashilbe.review.dto.SearchResponseDto;
import com.example.dongnemashilbe.review.service.SearchService;
import com.example.dongnemashilbe.security.impl.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;
    // 검색기능
    @ExeTimer
    @GetMapping("")
    public Page<SearchResponseDto> search(@RequestParam(value = "type", defaultValue = "likes") String type,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(required = false) String tag,
                                          @RequestParam String q,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws JsonProcessingException {
        try{
            userDetails.getUser();
        }catch (NullPointerException e){
            return searchService.search(type,page, q, tag,null);
        }
        return searchService.search(type, page, q, tag, userDetails.getUser());
    }
    // 반경검색
    @GetMapping("/nearby")
    public List<RadiusResponseDto> searchRadius(@RequestParam double latitude,
                                                @RequestParam double longitude,
                                                @RequestParam double radius){
        return searchService.searchRadius(latitude,longitude,radius);
    }

}

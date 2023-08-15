package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Tag;
import lombok.Getter;

@Getter
public class TagResponseDto {

    private Long id;
    private String name;

    public TagResponseDto(Tag tag){
        this.id = tag.getId();
        this.name = tag.getName();
    }
}

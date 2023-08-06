package com.example.dongnemashilbe.review.dto;

import com.example.dongnemashilbe.review.entity.Tag;
import lombok.Getter;

@Getter
public class TagResponseDTO {

    private Long id;
    private String name;

    public TagResponseDTO(Tag tag){
        this.id = tag.getId();
        this.name = tag.getName();
    }
}

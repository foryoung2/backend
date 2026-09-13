package com.foryoung.foryoung.performance.setlist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SetlistUpdateRequest {


    @NotBlank
    private String title;

    @NotBlank
    private String artist;


}
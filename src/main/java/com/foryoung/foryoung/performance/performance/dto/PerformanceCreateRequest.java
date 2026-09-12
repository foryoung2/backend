package com.foryoung.foryoung.performance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerformanceCreateRequest {


    @NotNull(message = "Venue ID must not be null")
    private Long venueId;

    @NotBlank(message = "Title must not be blank")
    private String title;

    private String artist;


}
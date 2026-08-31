package com.foryoung.foryoung.venue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VenueSeatViewCreateRequest {


    @NotBlank(message = "Seat information must not be blank")
    private String seatInfo;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String content;


}
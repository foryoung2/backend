package com.foryoung.foryoung.venue.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VenueCreateRequest {


    @NotBlank(message = "Venue name must not be blank")
    private String name;


}
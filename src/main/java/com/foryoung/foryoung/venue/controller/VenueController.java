package com.foryoung.foryoung.venue.controller;

import com.foryoung.foryoung.venue.dto.VenueCreateRequest;
import com.foryoung.foryoung.venue.dto.VenueResponse;
import com.foryoung.foryoung.venue.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/venues")
public class VenueController {


    private final VenueService venueService;


    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(@Valid @RequestBody VenueCreateRequest request) {

        VenueResponse response = venueService.createVenue(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


}
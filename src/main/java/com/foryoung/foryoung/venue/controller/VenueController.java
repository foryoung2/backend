package com.foryoung.foryoung.venue.controller;

import com.foryoung.foryoung.venue.dto.VenueSearchResponse;
import com.foryoung.foryoung.venue.service.VenueService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/venues")
public class VenueController {


    private final VenueService venueService;


    @GetMapping("/search")
    public ResponseEntity<Page<VenueSearchResponse>> searchVenues(@RequestParam @NotBlank(message = "Keyword must not be blank")
                                                                      String keyword,
                                                                  @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(venueService.searchVenues(keyword, pageable));

    }


}
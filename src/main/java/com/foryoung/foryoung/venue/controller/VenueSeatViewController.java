package com.foryoung.foryoung.venue.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.venue.dto.VenueSeatViewCreateRequest;
import com.foryoung.foryoung.venue.dto.VenueSeatViewResponse;
import com.foryoung.foryoung.venue.service.VenueSeatViewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venue-views")
public class VenueSeatViewController {


    private final VenueSeatViewService seatViewService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VenueSeatViewResponse> createVenueSeatView(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @Valid @RequestPart("data") VenueSeatViewCreateRequest request,
                                                                     @RequestPart("images") List<MultipartFile> images) {

        VenueSeatViewResponse response = seatViewService.createVenueSeatView(userDetails.getMemberId(), request, images);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    @GetMapping("/{venueViewId}")
    public ResponseEntity<VenueSeatViewResponse> getVenueSeatView(@PathVariable Long venueViewId) {

        return ResponseEntity.ok(seatViewService.getVenueSeatView(venueViewId));

    }


    @GetMapping("/{venueId}/views")
    public ResponseEntity<Page<VenueSeatViewResponse>> getVenueSeatViews(@PathVariable Long venueId,
                                                                         @PageableDefault(size = 20) Pageable pageable) {

        Page<VenueSeatViewResponse> response = seatViewService.getVenueSeatViews(venueId, pageable);

        return ResponseEntity.ok(response);

    }


    @GetMapping("/my")
    public ResponseEntity<Page<VenueSeatViewResponse>> getMyVenueSeatViews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @PageableDefault(size = 10) Pageable pageable) {

        Page<VenueSeatViewResponse> response = seatViewService.getMyVenueSeatViews(userDetails.getMemberId(), pageable);

        return ResponseEntity.ok(response);

    }


    @DeleteMapping("/{venueViewId}")
    public ResponseEntity<Void> deleteVenueSeatView(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable Long venueViewId) {

        seatViewService.deleteVenueSeatView(userDetails.getMemberId(), venueViewId);

        return ResponseEntity.noContent().build();

    }


}
package com.foryoung.foryoung.venue.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.global.pagination.PageResponse;
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
@RequestMapping("/venues")
public class VenueSeatViewController {

    private final VenueSeatViewService seatViewService;


    @PostMapping(
            value = "/{venueId}/seat-views",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<VenueSeatViewResponse> createVenueSeatView(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @PathVariable Long venueId,
                                                                     @Valid @RequestPart("data") VenueSeatViewCreateRequest request,
                                                                     @RequestPart("images") List<MultipartFile> images) {

        VenueSeatViewResponse response =
                seatViewService.createVenueSeatView(userDetails.getMemberId(), venueId, request, images);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    @GetMapping("/seat-views/{venueViewId}")
    public ResponseEntity<VenueSeatViewResponse> getVenueSeatView(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable Long venueViewId) {

        Long memberId = userDetails != null
                ? userDetails.getMemberId()
                : null;

        VenueSeatViewResponse response = seatViewService.getVenueSeatView(venueViewId, memberId);

        return ResponseEntity.ok(response);

    }


    @GetMapping("/{venueId}/seat-views")
    public ResponseEntity<PageResponse<VenueSeatViewResponse>> getVenueSeatViews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                 @PathVariable Long venueId,
                                                                                 @PageableDefault(size = 12) Pageable pageable) {

        Long memberId = userDetails != null
                ? userDetails.getMemberId()
                : null;

        Page<VenueSeatViewResponse> response = seatViewService.getVenueSeatViews(venueId, memberId, pageable);

        return ResponseEntity.ok(PageResponse.from(response));

    }


    @GetMapping("/seat-views/me")
    public ResponseEntity<PageResponse<VenueSeatViewResponse>> getMyVenueSeatViews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                   @PageableDefault(size = 12) Pageable pageable) {

        Page<VenueSeatViewResponse> response =
                seatViewService.getMyVenueSeatViews(userDetails.getMemberId(), pageable);

        return ResponseEntity.ok(PageResponse.from(response));

    }


    @DeleteMapping("/seat-views/{venueViewId}")
    public ResponseEntity<Void> deleteVenueSeatView(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable Long venueViewId) {

        seatViewService.deleteVenueSeatView(userDetails.getMemberId(), venueViewId);

        return ResponseEntity.noContent().build();

    }


}
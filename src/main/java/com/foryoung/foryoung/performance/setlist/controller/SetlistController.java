package com.foryoung.foryoung.performance.setlist.controller;

import com.foryoung.foryoung.performance.setlist.dto.*;
import com.foryoung.foryoung.performance.setlist.service.SetlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance-schedules")
public class SetlistController {


    private final SetlistService setlistService;

    @PostMapping("/{scheduleId}/setlists")
    public ResponseEntity<SetlistResponse> createSetlist(@PathVariable Long scheduleId,
                                                         @Valid @RequestBody SetlistCreateRequest request) {

        SetlistResponse response = setlistService.createSetlist(scheduleId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    @PatchMapping("/{scheduleId}/setlists/{setlistId}")
    public ResponseEntity<SetlistResponse> updateSetlist(@PathVariable Long scheduleId,
                                                         @PathVariable Long setlistId,
                                                         @Valid @RequestBody SetlistUpdateRequest request) {

        SetlistResponse response = setlistService.updateSetlist(scheduleId, setlistId, request);

        return ResponseEntity.ok(response);

    }


    @PutMapping("/{scheduleId}/setlists/reorder")
    public ResponseEntity<Void> reorderSetlists(@PathVariable Long scheduleId,
                                                @Valid @RequestBody SetlistReorderRequest request) {
        setlistService.reorderSetlists(scheduleId, request.getSetlistIds());

        return ResponseEntity.noContent().build();

    }


    @GetMapping("/{scheduleId}/setlists")
    public ResponseEntity<List<SetlistResponse>> getSetlists(@PathVariable Long scheduleId) {

        return ResponseEntity.ok(setlistService.getSetlists(scheduleId));

    }


    @DeleteMapping("/{scheduleId}/setlists/{setlistId}")
    public ResponseEntity<Void> deleteSetlist(@PathVariable Long scheduleId,
                                              @PathVariable Long setlistId) {

        setlistService.deleteSetlist(scheduleId, setlistId);

        return ResponseEntity.noContent().build();

    }



}
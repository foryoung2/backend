package com.foryoung.foryoung.ocr.controller;

import com.foryoung.foryoung.ocr.dto.PerformanceOcrResponse;
import com.foryoung.foryoung.ocr.service.PerformanceOcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ocr")
public class OcrController {


    private final PerformanceOcrService performanceOcrService;


    @PostMapping("/performances")
    public ResponseEntity<PerformanceOcrResponse> extractPerformance(@RequestParam("image") MultipartFile image) {

        PerformanceOcrResponse response = performanceOcrService.extractPerformance(image);

        return ResponseEntity.ok(response);

    }


}
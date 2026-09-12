package com.foryoung.foryoung.ocr.service;

import com.foryoung.foryoung.ocr.parser.PerformanceOcrParser;
import com.foryoung.foryoung.ocr.dto.PerformanceOcrResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PerformanceOcrService {


    private final TesseractOcrService tesseractOcrService;
    private final PerformanceOcrParser ocrParser;


    public PerformanceOcrResponse extractPerformance(MultipartFile image) {

        String rawText = tesseractOcrService.extractText(image);

        return ocrParser.parse(rawText);

    }


}
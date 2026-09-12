package com.foryoung.foryoung.ocr.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TesseractOcrService {


    private final Tesseract tesseract;


    public String extractText(MultipartFile image) {

        if (image.isEmpty()) {
            throw new CustomException(ErrorCode.OCR_IMAGE_EMPTY);
        }

        try {

            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            if (bufferedImage == null) {
                throw new CustomException(ErrorCode.OCR_IMAGE_READ_FAILED);
            }

            return tesseract.doOCR(bufferedImage);

        } catch (IOException e) {

            log.error(
                    "Failed to read OCR image. filename={}",
                    image.getOriginalFilename(),
                    e
            );

            throw new CustomException(ErrorCode.OCR_IMAGE_READ_FAILED);

        } catch (TesseractException e) {

            log.error(
                    "Tesseract OCR processing failed. filename={}",
                    image.getOriginalFilename(),
                    e
            );

            throw new CustomException(ErrorCode.OCR_PROCESS_FAILED);
        }

    }


}
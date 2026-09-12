package com.foryoung.foryoung.ocr.config;

import com.foryoung.foryoung.ocr.properties.TesseractProperties;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TesseractProperties.class)
public class TesseractConfig {


    @Bean
    public Tesseract tesseract(TesseractProperties properties) {

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(properties.getDatapath());
        tesseract.setLanguage(properties.getLanguage());

        return tesseract;

    }


}
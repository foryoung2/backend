package com.foryoung.foryoung.ocr.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ocr.tesseract")
public class TesseractProperties {


    private String datapath;

    private String language;


}
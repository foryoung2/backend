package com.foryoung.foryoung.search.dto;

import com.foryoung.foryoung.search.document.PerformanceDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PerformanceSearchResult {


    private List<PerformanceDocument> documents;

    private long totalElements;


}
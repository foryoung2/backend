package com.foryoung.foryoung.search.dto;

import com.foryoung.foryoung.search.document.VenueDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VenueSearchResult {


    private List<VenueDocument> documents;

    private long totalElements;


}
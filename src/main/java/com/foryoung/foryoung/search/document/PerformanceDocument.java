package com.foryoung.foryoung.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDocument {


    private Long id;

    private String title;

    private String artist;

    private String venue;

    private String posterImageUrl;


}
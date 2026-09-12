package com.foryoung.foryoung.performance.record.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerformanceRecordUpdateRequest {


    @PositiveOrZero(message = "Ticket price must be zero or greater")
    private Integer ticketPrice;

    private String seat;

    @Min(1)
    @Max(5)
    private Integer rating;


}
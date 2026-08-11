package com.foryoung.foryoung.performance.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerformanceRecordUpdateRequest {


    @PositiveOrZero(message = "Ticket price must be zero or greater")
    private Integer ticketPrice;

    private String seat;


}
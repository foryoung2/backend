package com.foryoung.foryoung.performance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PerformanceScheduleCreateRequest {


    @NotNull(message = "Performance date and time must not be null")
    private LocalDateTime performanceDateTime;


}
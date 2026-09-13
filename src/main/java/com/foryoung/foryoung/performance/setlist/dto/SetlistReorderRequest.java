package com.foryoung.foryoung.performance.setlist.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SetlistReorderRequest {


    @NotEmpty
    private List<@NotNull Long> setlistIds;


}
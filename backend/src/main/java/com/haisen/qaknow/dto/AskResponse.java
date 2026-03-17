package com.haisen.qaknow.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AskResponse {
    private String answer;
    private List<CitationDto> citations;
}

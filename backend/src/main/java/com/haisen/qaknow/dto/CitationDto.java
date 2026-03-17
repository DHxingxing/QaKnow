package com.haisen.qaknow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CitationDto {
    private String documentName;
    private String chapterTitle;
    private Integer chunkOrder;
    private String snippet;
}

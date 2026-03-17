package com.haisen.qaknow.controller;

import com.haisen.qaknow.common.ApiResponse;
import com.haisen.qaknow.domain.KbDocument;
import com.haisen.qaknow.domain.KbInterviewQuestion;
import com.haisen.qaknow.domain.KbSummary;
import com.haisen.qaknow.service.DocumentService;
import com.haisen.qaknow.service.KnowledgeExtraService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final KnowledgeExtraService knowledgeExtraService;

    public DocumentController(DocumentService documentService, KnowledgeExtraService knowledgeExtraService) {
        this.documentService = documentService;
        this.knowledgeExtraService = knowledgeExtraService;
    }

    @PostMapping("/upload")
    public ApiResponse<KbDocument> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success(documentService.upload(file));
    }

    @GetMapping
    public ApiResponse<List<KbDocument>> list() {
        return ApiResponse.success(documentService.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<KbDocument> detail(@PathVariable Long id) {
        return ApiResponse.success(documentService.detail(id));
    }

    @PostMapping("/{id}/parse")
    public ApiResponse<Integer> parse(@PathVariable Long id) throws IOException {
        return ApiResponse.success(documentService.parse(id));
    }

    @PostMapping("/{id}/index")
    public ApiResponse<Integer> index(@PathVariable Long id) {
        return ApiResponse.success(documentService.index(id));
    }

    @PostMapping("/{id}/summary")
    public ApiResponse<KbSummary> summary(@PathVariable Long id) {
        return ApiResponse.success(knowledgeExtraService.generateSummary(id));
    }

    @PostMapping("/{id}/interview-questions")
    public ApiResponse<List<KbInterviewQuestion>> interview(@PathVariable Long id) {
        return ApiResponse.success(knowledgeExtraService.generateInterview(id));
    }
}

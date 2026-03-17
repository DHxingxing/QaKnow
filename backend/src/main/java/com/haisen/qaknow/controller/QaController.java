package com.haisen.qaknow.controller;

import com.haisen.qaknow.common.ApiResponse;
import com.haisen.qaknow.domain.KbQaRecord;
import com.haisen.qaknow.dto.AskRequest;
import com.haisen.qaknow.dto.AskResponse;
import com.haisen.qaknow.service.QaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qa")
public class QaController {
    private final QaService qaService;

    public QaController(QaService qaService) {
        this.qaService = qaService;
    }

    @PostMapping("/ask")
    public ApiResponse<AskResponse> ask(@Valid @RequestBody AskRequest request) {
        return ApiResponse.success(qaService.ask(request.getQuestion()));
    }

    @GetMapping("/history")
    public ApiResponse<List<KbQaRecord>> history() {
        return ApiResponse.success(qaService.history());
    }
}

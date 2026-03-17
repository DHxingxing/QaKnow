package com.haisen.qaknow.service;

import com.haisen.qaknow.domain.KbInterviewQuestion;
import com.haisen.qaknow.domain.KbSummary;

import java.util.List;

public interface KnowledgeExtraService {
    KbSummary generateSummary(Long documentId);
    List<KbInterviewQuestion> generateInterview(Long documentId);
}

package com.haisen.qaknow.service;

import com.haisen.qaknow.dto.AskResponse;
import com.haisen.qaknow.domain.KbQaRecord;

import java.util.List;

public interface QaService {
    AskResponse ask(String question);
    List<KbQaRecord> history();
}

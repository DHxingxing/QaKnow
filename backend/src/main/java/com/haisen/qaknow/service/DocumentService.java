package com.haisen.qaknow.service;

import com.haisen.qaknow.domain.KbDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    KbDocument upload(MultipartFile file) throws IOException;
    List<KbDocument> list();
    KbDocument detail(Long id);
    int parse(Long id) throws IOException;
    int index(Long id);
}

package com.haisen.qaknow.repository;

import com.haisen.qaknow.domain.KbChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KbChunkRepository extends JpaRepository<KbChunk, Long> {
    List<KbChunk> findByDocumentIdOrderByChunkOrderAsc(Long documentId);
}

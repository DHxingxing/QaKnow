package com.haisen.qaknow.repository;

import com.haisen.qaknow.domain.KbSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KbSummaryRepository extends JpaRepository<KbSummary, Long> {
    Optional<KbSummary> findTopByDocumentIdOrderByCreatedAtDesc(Long documentId);
}

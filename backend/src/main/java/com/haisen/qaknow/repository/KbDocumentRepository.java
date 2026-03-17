package com.haisen.qaknow.repository;

import com.haisen.qaknow.domain.KbDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KbDocumentRepository extends JpaRepository<KbDocument, Long> {
}

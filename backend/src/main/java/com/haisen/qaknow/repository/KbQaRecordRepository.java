package com.haisen.qaknow.repository;

import com.haisen.qaknow.domain.KbQaRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KbQaRecordRepository extends JpaRepository<KbQaRecord, Long> {
    List<KbQaRecord> findAllByOrderByCreatedAtDesc();
}

package com.haisen.qaknow.repository;

import com.haisen.qaknow.domain.KbInterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KbInterviewQuestionRepository extends JpaRepository<KbInterviewQuestion, Long> {
    List<KbInterviewQuestion> findByDocumentIdOrderByIdDesc(Long documentId);
}

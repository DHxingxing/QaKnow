package com.haisen.qaknow.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kb_qa_record")
public class KbQaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(name = "citations_json", columnDefinition = "TEXT")
    private String citationsJson;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

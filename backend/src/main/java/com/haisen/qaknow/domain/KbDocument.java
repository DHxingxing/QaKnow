package com.haisen.qaknow.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kb_document")
public class KbDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private Long size;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "parse_status")
    private String parseStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

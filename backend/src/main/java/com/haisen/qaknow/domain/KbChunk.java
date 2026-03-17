package com.haisen.qaknow.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "kb_chunk")
public class KbChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "chapter_title")
    private String chapterTitle;

    @Column(name = "chunk_text", columnDefinition = "TEXT")
    private String chunkText;

    @Column(name = "chunk_order")
    private Integer chunkOrder;

    @Column(name = "start_pos")
    private Integer startPos;

    @Column(name = "end_pos")
    private Integer endPos;

    private Integer page;
}

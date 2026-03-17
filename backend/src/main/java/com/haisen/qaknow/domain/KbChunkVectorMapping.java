package com.haisen.qaknow.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "kb_chunk_vector_mapping")
public class KbChunkVectorMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chunk_id")
    private Long chunkId;

    @Column(name = "vector_data", columnDefinition = "TEXT")
    private String vectorData;

    @Column(name = "vector_dim")
    private Integer vectorDim;
}

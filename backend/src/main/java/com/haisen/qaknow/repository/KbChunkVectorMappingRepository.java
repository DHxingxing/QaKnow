package com.haisen.qaknow.repository;

import com.haisen.qaknow.domain.KbChunkVectorMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KbChunkVectorMappingRepository extends JpaRepository<KbChunkVectorMapping, Long> {
    List<KbChunkVectorMapping> findByChunkIdIn(List<Long> chunkIds);
    void deleteByChunkIdIn(List<Long> chunkIds);
}

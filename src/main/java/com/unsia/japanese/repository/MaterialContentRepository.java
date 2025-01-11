package com.unsia.japanese.repository;

import com.unsia.japanese.entity.Material;
import com.unsia.japanese.entity.MaterialContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialContentRepository extends JpaRepository<MaterialContent, String> {
    List<MaterialContent> findByMaterialId(Material materialId);
}


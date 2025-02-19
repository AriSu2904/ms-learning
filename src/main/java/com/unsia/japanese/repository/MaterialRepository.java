package com.unsia.japanese.repository;

import com.unsia.japanese.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, String> {
    Optional<Material> findByName(String name);
}

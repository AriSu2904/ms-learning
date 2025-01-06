package com.unsia.japanese.repository;

import com.unsia.japanese.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}

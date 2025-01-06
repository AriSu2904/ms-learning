package com.unsia.japanese.repository;

import com.unsia.japanese.entity.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioRepository extends JpaRepository<Audio, String> {
}

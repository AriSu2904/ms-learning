package com.unsia.japanese.repository;

import com.unsia.japanese.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}

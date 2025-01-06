package com.unsia.japanese.repository;

import com.unsia.japanese.entity.Hirakana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HirakanaRepository extends JpaRepository<Hirakana, String> {
    List<Hirakana> findAllByType(String type);
}

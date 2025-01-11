package com.unsia.japanese.repository;

import com.unsia.japanese.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, String> {
}

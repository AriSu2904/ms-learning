package com.unsia.japanese.repository;

import com.unsia.japanese.entity.Character;
import com.unsia.japanese.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, String> {
    List<Character> findCharactersByMaterial(Material material);
    List<Character> findCharactersByMaterialAndLevel(Material material, Integer level);
}

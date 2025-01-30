package com.unsia.japanese.service.Impl;

import com.unsia.japanese.dto.request.MaterialRequest;
import com.unsia.japanese.dto.response.MaterialResponse;
import com.unsia.japanese.entity.Material;
import com.unsia.japanese.repository.MaterialRepository;
import com.unsia.japanese.service.MaterialService;
import com.unsia.japanese.utils.CredentialValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    public MaterialResponse create(MaterialRequest material) {
        String name = material.getName();
        log.info("Creating material with name: {}", name);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //validate role first
        CredentialValidator.isValidAuthor(authentication);

        Material objMaterial = Material.builder()
                .name(material.getName().toUpperCase())
                .description(material.getDescription())
                .order(material.getOrder())
                .build();

        Material savedMaterial = materialRepository.save(objMaterial);

        return savedMaterial.toResponse();
    }

    private List<Material> generateMaterials() {
        log.info("Generating materials");

        Material material1 = Material.builder()
                .name("HIRAGANA")
                .original("ひらがな")
                .description("Hiragana is a Japanese syllabary, one basic component of the Japanese writing system.")
                .order(1)
                .build();

        Material material2 = Material.builder()
                .name("KATAKANA")
                .original("カタカナ")
                .description("Katakana is a Japanese syllabary, one basic component of the Japanese writing system.")
                .order(2)
                .build();

        Material material3 = Material.builder()
                .name("KANJI N5")
                .original("漢字 N5")
                .description("Kanji N5 is the first level of the Japanese Language Proficiency Test (JLPT).")
                .order(3)
                .build();

        return List.of(material1, material2, material3);
    }

    @Override
    public List<MaterialResponse> getAll() {
        log.info("Getting all materials");

        List<Material> Materials = materialRepository.findAll();

        if(Materials.isEmpty()) {
            Materials = generateMaterials();

            materialRepository.saveAll(Materials);
        }

        return Materials.stream()
                .map(Material::toResponse)
                .sorted(Comparator.comparing(MaterialResponse::getOrder))
                .toList();
    }

    @Override
    public Material findByName(String name) {
        return materialRepository.findByName(name.toUpperCase()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material not found"));
    }
}

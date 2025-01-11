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

    @Override
    public List<MaterialResponse> getAll() {
        log.info("Getting all materials");

        List<Material> Materials = materialRepository.findAll();

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

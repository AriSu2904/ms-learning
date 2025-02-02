package com.unsia.japanese.service.Impl;

import com.unsia.japanese.dto.request.MaterialContentRequest;
import com.unsia.japanese.dto.response.MaterialContentResponse;
import com.unsia.japanese.entity.Material;
import com.unsia.japanese.entity.MaterialContent;
import com.unsia.japanese.repository.MaterialContentRepository;
import com.unsia.japanese.service.MaterialContentService;
import com.unsia.japanese.service.MaterialService;
import com.unsia.japanese.utils.CredentialValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MaterialContentServiceImpl implements MaterialContentService {

    private final MaterialContentRepository materialContentRepository;
    private final MaterialService materialService;

    @Override
    public List<MaterialContentResponse> save(MaterialContentRequest materialContent) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //validate role first
        CredentialValidator.isValidAuthor(authentication);

        Material material = materialService.findByName(materialContent.getMaterialParent());

        List<MaterialContent> materialContents = generateContent(material);

        return materialContentRepository.saveAll(materialContents).stream()
                .map(MaterialContent::toResponse)
                .sorted(Comparator.comparing(MaterialContentResponse::getPosition))
                .toList();
    }

    @Override
    public List<MaterialContentResponse> getContent(String name) {
        Material material = materialService.findByName(name);

        return generateContent(material).stream()
                .map(MaterialContent::toResponse)
                .sorted(Comparator.comparing(MaterialContentResponse::getPosition))
                .toList();
    }

    private List<MaterialContent> generateContent(Material material) {
        List<String> imageUri = imageBackground();
        List<MaterialContent> contents = List.of(
            createMaterialContent(material, "Letters", imageUri.get(0), "main letters of ", 1),
            createMaterialContent(material, "Ten&Maru", imageUri.get(1), "voicing mark of ", 2),
            createMaterialContent(material, "Yōon", imageUri.get(2), "semi-voicing mark of ", 3),
            createMaterialContent(material, "Quiz", imageUri.get(3), "Test your knowledge of ", 4)
        );
        return new ArrayList<>(contents);
    }

    private MaterialContent createMaterialContent(Material material, String name, String imageUri, String descriptionPrefix, int position) {
        return MaterialContent.builder()
                .materialId(material)
                .name(name)
                .imageUri(imageUri)
                .description(descriptionPrefix + material.getName().toLowerCase())
                .position(position)
                .build();
    }

    private List<String> imageBackground() {
        String BASE_URL = "https://jpn-bucket.s3.ap-southeast-2.amazonaws.com/materials/";

        List<String> FILE_NAMES = List.of(
                "hira-ke", "hira-ku", "hira-ki", "hira-u", "hira-i", "hira-a", "hira-to", "hira-ka", "hira-e", "hira-o"
        );

        List<String> shuffledFileNames = new ArrayList<>(FILE_NAMES);
        Collections.shuffle(shuffledFileNames);
        List<String> randomUrls = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            randomUrls.add(BASE_URL + shuffledFileNames.get(i) + ".jpg");
        }

        return randomUrls;
    }
}

package com.unsia.japanese.service.Impl;

import com.unsia.japanese.dto.request.MaterialContentRequest;
import com.unsia.japanese.dto.response.MaterialContentResponse;
import com.unsia.japanese.entity.Material;
import com.unsia.japanese.entity.MaterialContent;
import com.unsia.japanese.repository.MaterialContentRepository;
import com.unsia.japanese.service.MaterialContentService;
import com.unsia.japanese.service.MaterialService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MaterialContentServiceImpl implements MaterialContentService {

    private final MaterialContentRepository materialContentRepository;
    private final MaterialService materialService;

    @Override
    public List<MaterialContentResponse> save(MaterialContentRequest materialContent) {
        Material material = materialService.findByName(materialContent.getMaterialParent());

        List<MaterialContent> materialContents = generateContent(materialContent, material);

        return materialContentRepository.saveAll(materialContents).stream()
                .map(MaterialContent::toResponse)
                .sorted(Comparator.comparing(MaterialContentResponse::getPosition))
                .toList();
    }

    @Override
    public List<MaterialContentResponse> getContent(String name) {
        Material material = materialService.findByName(name);

        return materialContentRepository.findByMaterialId(material).stream()
                .map(MaterialContent::toResponse)
                .sorted(Comparator.comparing(MaterialContentResponse::getPosition))
                .toList();
    }

    private List<MaterialContent> generateContent(MaterialContentRequest request, Material material) {
        List<MaterialContent> contents = new ArrayList<>();

        if(request.isRequiredLetters()) {
            MaterialContent withLetters = MaterialContent.builder()
                    .materialId(material)
                    .name("Letters")
                    .description("Letters for learning")
                    .position(0)
                    .build();

            contents.add(withLetters);
        }

        if(request.isRequiredQuizzes()) {
            MaterialContent withQuizzes = MaterialContent.builder()
                    .materialId(material)
                    .name("Quiz")
                    .description("Quizzes for learning")
                    .position(1)
                    .build();

            contents.add(withQuizzes);
        }

        if(request.isRequiredEasyLearn()) {
            MaterialContent withEasyLearn = MaterialContent.builder()
                    .materialId(material)
                    .name("Easy Learn")
                    .description("Easy learning for beginners")
                    .position(2)
                    .build();

            contents.add(withEasyLearn);
        }

        if(request.isRequiredTest()) {
            MaterialContent withTest = MaterialContent.builder()
                    .materialId(material)
                    .name("Test")
                    .description("Test for learning")
                    .position(3)
                    .build();

            contents.add(withTest);
        }

        return contents;
    }
}

package com.unsia.japanese.service.Impl;

import com.unsia.japanese.dto.request.CharacterRequest;
import com.unsia.japanese.dto.response.CharacterResponse;
import com.unsia.japanese.entity.Audio;
import com.unsia.japanese.entity.Character;
import com.unsia.japanese.entity.Image;
import com.unsia.japanese.entity.Material;
import com.unsia.japanese.repository.CharacterRepository;
import com.unsia.japanese.service.AudioService;
import com.unsia.japanese.service.CharacterService;
import com.unsia.japanese.service.ImageService;
import com.unsia.japanese.service.MaterialService;
import com.unsia.japanese.utils.CredentialValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final MaterialService materialService;
    private final ImageService imageService;
    private final AudioService audioService;

    @Override
    public CharacterResponse save(CharacterRequest request, List<MultipartFile> files) {
        log.info("Try to save character with name: {}", request.getRomaji());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //validate role first
        CredentialValidator.isValidAuthor(authentication);

        Material material = materialService.findByName(request.getParentName());

        Image savedImg = imageService.save(files.get(0));
        Audio savedAudio = audioService.save(files.get(1));

        Character newChar = Character.builder()
                .material(material)
                .character(request.getCharacter())
                .romaji(request.getRomaji())
                .stroke(request.getStroke())
                .mean(request.getMean())
                .level(request.getLevel())
                .image(savedImg)
                .audio(savedAudio)
                .order(request.getOrder())
                .build();

        Character savedChar = characterRepository.save(newChar);

        return savedChar.toResponse();
    }


    @Override
    public List<CharacterResponse> getLetters(String name) {
        return List.of();
    }
}

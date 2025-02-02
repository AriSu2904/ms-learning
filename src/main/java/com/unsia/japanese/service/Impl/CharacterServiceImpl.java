package com.unsia.japanese.service.Impl;

import com.unsia.japanese.dto.request.CharacterRequest;
import com.unsia.japanese.dto.response.CharacterResponse;
import com.unsia.japanese.entity.*;
import com.unsia.japanese.entity.Character;
import com.unsia.japanese.repository.CharacterRepository;
import com.unsia.japanese.service.AudioService;
import com.unsia.japanese.service.CharacterService;
import com.unsia.japanese.service.ImageService;
import com.unsia.japanese.service.MaterialService;
import com.unsia.japanese.utils.CredentialValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final MaterialService materialService;
    private final ImageService imageService;
    private final AudioService audioService;

    @Value(("${unsia.hirakana.base_url.audio}"))
    private String audioUri;

    @Value(("${unsia.hirakana.base_url.image}"))
    private String imageUri;

    @Value(("${unsia.hirakana.base_url.image2}"))
    private String secondImageUri;

    @Value(("${unsia.hirakana.base_url.image_detail}"))
    private String secondImageDetailUri;

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
    public CharacterResponse scrapping(CharacterRequest request) {
        log.info("Try to scrap character with name: {}", request.getRomaji());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //validate role first
        CredentialValidator.isValidAuthor(authentication);

        Material material = materialService.findByName(request.getParentName());

        String audio = constructUrl(request.getRomaji(),".mp3", material.getName());
        String image = constructUrl(request.getRomaji(), ".png", material.getName());

        Character newChar = Character.builder()
                .material(material)
                .character(request.getCharacter())
                .romaji(request.getRomaji())
                .stroke(request.getStroke())
                .mean(request.getMean())
                .level(request.getLevel())
                .order(request.getOrder())
                .audio(Audio.builder().path(audio).build())
                .image(Image.builder().path(image).build())
                .build();

        Character savedChar = characterRepository.save(newChar);

        return savedChar.toResponse();
    }

    @Override
    public List<CharacterResponse> scrappingBulkInsert(List<CharacterRequest> requests) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //validate role first
        CredentialValidator.isValidAuthor(authentication);

        Material material = materialService.findByName(requests.get(0).getParentName());

        List<Character> characters = requests.stream()
                .map(request -> {
                    String audio = constructUrl(request.getRomaji(),".mp3", material.getName());
                    String image = constructUrl(request.getRomaji(), ".png", material.getName());
                    String secondImage = constructNHKImage(request.getRomaji(), ".png", material.getName(), false);
                    String secondImageDetail = constructNHKImage(request.getRomaji(), ".png", material.getName(), true);

                    return Character.builder()
                            .material(material)
                            .character(request.getCharacter())
                            .romaji(request.getRomaji())
                            .stroke(request.getStroke())
                            .mean(request.getMean())
                            .level(request.getLevel())
                            .section(request.getSection())
                            .order(request.getOrder())
                            .audio(Audio.builder().path(audio).build())
                            .image(Image.builder().path(image).build())
                            .secondImage(secondImage)
                            .secondImageDetail(secondImageDetail)
                            .build();
                })
                .toList();

        List<Character> savedCharacters = characterRepository.saveAll(characters);

        return savedCharacters.stream()
                .map(Character::toResponse)
                .sorted(Comparator.comparing(CharacterResponse::getOrder))
                .toList();
    }

    @Override
    public List<CharacterResponse> getLetters(String name, Integer level) {
        Material material = materialService.findByName(name);

        List<Character> characters;
        if(level != null) {
            characters = characterRepository.findCharactersByMaterialAndLevel(material, level);
        } else {
            characters = characterRepository.findCharactersByMaterial(material);
        }

        return characters.stream()
                .map(Character::toResponse)
                .sorted(Comparator.comparing(CharacterResponse::getOrder))
                .toList();
    }

    private String constructUrl(String name, String extension, String parent) {
        String lowerCaseName = name.toLowerCase();
        String fileName = lowerCaseName + extension;

        if(Objects.equals(extension, ".mp3")) {
            log.info("[ARI] Current audio url: " + this.audioUri);

            return audioUri + fileName;
        }

        String letter = parent.equals("hiragana".toUpperCase()) ? "hira" : "kata";
        fileName = letter + "_" + lowerCaseName + extension;

        return imageUri + fileName;
    }

    private String constructNHKImage(String romaji, String extension, String parent, boolean isDetail) {
        String lowerCaseName = romaji.toLowerCase();
        String fileName = lowerCaseName + extension;
        String pathUri = parent.equals("hiragana".toUpperCase()) ? "hira" : "kana";

        if(isDetail) {
           return this.secondImageDetailUri + pathUri + "/" + fileName;
        }

        return this.secondImageUri + pathUri + "/" + fileName;
    }
}

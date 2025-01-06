package com.unsia.japanese.service.Impl;

import com.unsia.japanese.entity.Audio;
import com.unsia.japanese.entity.File;
import com.unsia.japanese.entity.Hirakana;
import com.unsia.japanese.repository.AudioRepository;
import com.unsia.japanese.service.AudioService;
import com.unsia.japanese.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;
    private final FileService fileService;

    @Override
    public Audio save(Hirakana hirakana, MultipartFile multipartFile) {
        log.info("Try to save audio for hirakana: {}", hirakana.getName());

        try {
            File audioFile = fileService.create(multipartFile);

            Audio newAudio = Audio.builder()
                    .name(audioFile.getName())
                    .size(audioFile.getSize())
                    .contentType(audioFile.getContentType())
                    .path(audioFile.getPath())
                    .hirakana(hirakana)
                    .build();

            return audioRepository.save(newAudio);
        } catch (Exception e) {
            log.error("An error occurred while saving audio for hirakana: {}", hirakana.getName());
            throw e;
        }
    }

    @Override
    public Resource findById(String id) {
        var audio = audioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Audio not found")
        );

        return fileService.get(audio.getPath());
    }

    @Override
    public void deleteById(String id) {

    }
}

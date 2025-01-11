package com.unsia.japanese.service.Impl;

import com.unsia.japanese.entity.Audio;
import com.unsia.japanese.entity.File;
import com.unsia.japanese.repository.AudioRepository;
import com.unsia.japanese.service.AudioService;
import com.unsia.japanese.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;
    private final FileService fileService;

    @Override
    public Audio save(MultipartFile audio) {
        try {
            log.info("Saving audio with name: {}", audio.getName());

            File file = fileService.create(audio);

            Audio newAudio = Audio.builder()
                    .name(file.getName())
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .path(file.getPath())
                    .build();

            return audioRepository.save(newAudio);
        } catch (Exception e) {
            log.error("Error saving image with name: {}", audio.getName());
            throw e;
        }
    }
}

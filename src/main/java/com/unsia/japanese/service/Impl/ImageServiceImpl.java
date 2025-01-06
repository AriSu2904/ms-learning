package com.unsia.japanese.service.Impl;

import com.unsia.japanese.entity.File;
import com.unsia.japanese.entity.Hirakana;
import com.unsia.japanese.entity.Image;
import com.unsia.japanese.repository.ImageRepository;
import com.unsia.japanese.service.FileService;
import com.unsia.japanese.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;

    @Override
    public Image save(Hirakana hirakana, MultipartFile file) {
        log.info("Try to save image for hirakana: {}", hirakana.getName());

        try {
            File imageFile = fileService.create(file);

            Image newImage = Image.builder()
                    .name(imageFile.getName())
                    .size(imageFile.getSize())
                    .contentType(imageFile.getContentType())
                    .path(imageFile.getPath())
                    .hirakana(hirakana)
                    .build();

            return imageRepository.save(newImage);
        }catch (Exception e){
            log.error("An error occurred while saving image for hirakana: {}", hirakana.getName());
            throw e;
        }
    }

    @Override
    public Resource findById(String id) {
        var image = imageRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Image not found")
        );

        return fileService.get(image.getPath());
    }

    @Override
    public void deleteById(String id) {
        return;
    }
}

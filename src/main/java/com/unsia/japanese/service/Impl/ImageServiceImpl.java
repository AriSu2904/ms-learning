package com.unsia.japanese.service.Impl;

import com.unsia.japanese.entity.File;
import com.unsia.japanese.entity.Image;
import com.unsia.japanese.repository.ImageRepository;
import com.unsia.japanese.service.FileService;
import com.unsia.japanese.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;

    @Override
    public Image save(MultipartFile image) {
        try {
            log.info("Saving image with name: {}", image.getName());

            File file = fileService.create(image);

            Image newImg = Image.builder()
                    .name(file.getName())
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .path(file.getPath())
                    .build();

            return imageRepository.save(newImg);
        } catch (Exception e) {
            log.error("Error saving image with name: {}", image.getName());
            throw e;
        }
    }
}

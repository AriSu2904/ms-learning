package com.unsia.japanese.service.Impl;

import com.unsia.japanese.entity.File;
import com.unsia.japanese.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${unsia.file.url}")
    private String path;

    @Override
    public File create(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File can not be empty!");

        if (isSupportedContentType(multipartFile.getContentType()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid Content Type!");

        try {
            Path directoryPath = Paths.get(path);
            Files.createDirectories(directoryPath);
            String filename = String.format("%d_%s", System.currentTimeMillis(), multipartFile.getOriginalFilename());
            Path filePath = directoryPath.resolve(filename);
            Files.copy(multipartFile.getInputStream(), filePath);

            return File.builder()
                    .name(filename)
                    .path(filePath.toString())
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .build();
        } catch (IOException | RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "a failure occurred on the server");
        }
    }

    @Override
    public Resource get(String path) {
        Path filePath = Paths.get(path);
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "a failure occurred on the server");
        }
    }

    private Boolean isSupportedContentType(String contentType) {
        return !List.of(
                        "application/pdf",
                        "image/jpg", "image/png", "image/jpeg", "image/gif", "video/mp4",
                        "text/csv", "text/plain", "audio/mp4")
                .contains(contentType);
    }
}

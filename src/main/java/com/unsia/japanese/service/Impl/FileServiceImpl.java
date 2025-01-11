package com.unsia.japanese.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.unsia.japanese.entity.File;
import com.unsia.japanese.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value(("${aws.s3.bucket.name}"))
    private String bucket;

    //It will create new file without remove or delete the old file, always extending files (not replacing)
    @Override
    public File create(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File can not be empty!");

        if (isSupportedContentType(multipartFile.getContentType()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid Content Type!");

        try {
            String filename = String.format("%d_%s", System.currentTimeMillis(), multipartFile.getOriginalFilename());
            String folderPath = "materials/" + filename;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());

            amazonS3.putObject(bucket, folderPath, multipartFile.getInputStream(), metadata);
            String directoryPath = amazonS3.getUrl(bucket, folderPath).toString();

            return File.builder()
                    .name(filename)
                    .path(directoryPath)
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .build();
        } catch (IOException | RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "a failure occurred on the server");
        }
    }

    private Boolean isSupportedContentType(String contentType) {
        return !List.of(
                        "application/pdf",
                        "image/jpg", "image/png", "image/jpeg", "image/gif", "video/mp4",
                        "text/csv", "text/plain", "audio/mp4", "audio/mpeg", "audio/mp3", "audio/mpa", "audio/wav", "audio/aac")
                .contains(contentType);
    }
}

package com.unsia.japanese.service;


import com.unsia.japanese.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    File create(MultipartFile multipartFile);
    Resource get(String path);
}

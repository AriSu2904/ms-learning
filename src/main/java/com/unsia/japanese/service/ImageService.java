package com.unsia.japanese.service;

import com.unsia.japanese.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image save(MultipartFile image);
}

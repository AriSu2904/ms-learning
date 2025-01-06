package com.unsia.japanese.service;

import com.unsia.japanese.entity.Hirakana;
import com.unsia.japanese.entity.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image save(Hirakana hirakana, MultipartFile file);
    Resource findById(String id);
    void deleteById(String id);
}

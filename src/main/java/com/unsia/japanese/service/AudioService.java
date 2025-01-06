package com.unsia.japanese.service;

import com.unsia.japanese.entity.Audio;
import com.unsia.japanese.entity.Hirakana;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AudioService {
    Audio save(Hirakana hirakana, MultipartFile file);
    Resource findById(String id);
    void deleteById(String id);
}

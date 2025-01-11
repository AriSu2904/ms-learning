package com.unsia.japanese.service;

import com.unsia.japanese.entity.Audio;
import org.springframework.web.multipart.MultipartFile;

public interface AudioService {
    Audio save(MultipartFile audio);
}

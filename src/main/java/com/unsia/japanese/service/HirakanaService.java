package com.unsia.japanese.service;

import com.unsia.japanese.dto.request.HirakanaRequest;
import com.unsia.japanese.dto.response.HirakanaResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HirakanaService {
    List<HirakanaResponse> list(String type);
    HirakanaResponse detail(String id);
    HirakanaResponse create(HirakanaRequest request, List<MultipartFile> files);
    Resource loadImage(String id);
    Resource loadAudio(String id);
}

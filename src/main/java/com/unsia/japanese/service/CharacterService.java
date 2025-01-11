package com.unsia.japanese.service;

import com.unsia.japanese.dto.request.CharacterRequest;
import com.unsia.japanese.dto.response.CharacterResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CharacterService {
    CharacterResponse save(CharacterRequest request, List<MultipartFile> files);
    List<CharacterResponse> getLetters(String name);
}
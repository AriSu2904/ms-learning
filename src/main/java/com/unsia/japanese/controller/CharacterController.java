package com.unsia.japanese.controller;

import com.unsia.japanese.dto.common.CommonResponse;
import com.unsia.japanese.dto.request.CharacterRequest;
import com.unsia.japanese.dto.response.CharacterResponse;
import com.unsia.japanese.service.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/letters")
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping(
            headers = ("Content-Type=multipart/form-data"),
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CommonResponse<CharacterResponse>> create(
            @RequestPart(name = "desc") CharacterRequest characterRequest,
            @RequestPart(name = "files") List<MultipartFile> files
    ) {
        CharacterResponse characterResponse = characterService.save(characterRequest, files);

        CommonResponse<CharacterResponse> commonResponse = CommonResponse.<CharacterResponse>builder()
                .errors(null)
                .data(characterResponse)
                .build();

        return ResponseEntity.status(201).body(commonResponse);
    }

}

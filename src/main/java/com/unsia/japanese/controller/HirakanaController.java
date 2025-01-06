package com.unsia.japanese.controller;

import com.unsia.japanese.dto.common.CommonResponse;
import com.unsia.japanese.dto.request.HirakanaRequest;
import com.unsia.japanese.dto.response.HirakanaResponse;
import com.unsia.japanese.service.HirakanaService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/jps")
public class HirakanaController {

    private final HirakanaService hirakanaService;

    @GetMapping("/hirakana")
    public ResponseEntity<CommonResponse<List<HirakanaResponse>>> list(@RequestParam(required = true) String type) {
        List<HirakanaResponse> listHirakana = hirakanaService.list(type);

        CommonResponse<List<HirakanaResponse>> commonResponse = CommonResponse.<List<HirakanaResponse>>builder()
                .errors(null)
                .data(listHirakana)
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/hirakana/{id}")
    public ResponseEntity<CommonResponse<HirakanaResponse>> detail(@PathVariable String id) {
        HirakanaResponse response = hirakanaService.detail(id);

        CommonResponse<HirakanaResponse> commonResponse = CommonResponse.<HirakanaResponse>builder()
                .errors(null)
                .data(response)
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping(value = "/hirakana",
            headers = ("Content-Type=multipart/form-data"),
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CommonResponse<HirakanaResponse>> add(
            @RequestPart(name = "desc") HirakanaRequest request,
            @RequestPart(name = "files") List<MultipartFile> files) {
        HirakanaResponse response = hirakanaService.create(request, files);

        CommonResponse<HirakanaResponse> commonResponse = CommonResponse.<HirakanaResponse>builder()
                .errors(null)
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping("/hirakana/files/images/{id}")
    public ResponseEntity<?> loadImage(@PathVariable String id) {
        Resource resource = hirakanaService.loadImage(id);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/hirakana/files/audios/{id}")
    public ResponseEntity<?> loadAudio(@PathVariable String id) {
        Resource resource = hirakanaService.loadAudio(id);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

package com.unsia.japanese.controller;

import com.unsia.japanese.dto.common.CommonResponse;
import com.unsia.japanese.dto.request.MaterialRequest;
import com.unsia.japanese.dto.response.MaterialResponse;
import com.unsia.japanese.entity.Material;
import com.unsia.japanese.service.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/materials")
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )

    public ResponseEntity<CommonResponse<MaterialResponse>> create(@RequestBody MaterialRequest material) {
        MaterialResponse materialResponse = materialService.create(material);

        CommonResponse<MaterialResponse> commonResponse = CommonResponse.<MaterialResponse>builder()
                .errors(null)
                .data(materialResponse)
                .build();

        return ResponseEntity.status(201).body(commonResponse);
    }

    @GetMapping(
            produces = "application/json"
    )
    public ResponseEntity<CommonResponse<List<MaterialResponse>>> getAll() {
        List<MaterialResponse> all = materialService.getAll();

        CommonResponse<List<MaterialResponse>> commonResponse = CommonResponse.<List<MaterialResponse>>builder()
                .errors(null)
                .data(all)
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping(
            value = "/{name}",
            produces = "application/json"
    )
    public ResponseEntity<CommonResponse<MaterialResponse>> getById(@PathVariable String name) {
        Material material = materialService.findByName(name);

        CommonResponse<MaterialResponse> commonResponse = CommonResponse.<MaterialResponse>builder()
                .errors(null)
                .data(material.toResponse())
                .build();

        return ResponseEntity.ok(commonResponse);
    }
}

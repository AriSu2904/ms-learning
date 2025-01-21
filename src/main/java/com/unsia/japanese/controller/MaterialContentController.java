package com.unsia.japanese.controller;

import com.unsia.japanese.dto.common.CommonResponse;
import com.unsia.japanese.dto.request.MaterialContentRequest;
import com.unsia.japanese.dto.response.MaterialContentResponse;
import com.unsia.japanese.service.MaterialContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/contents")
public class MaterialContentController {

    private final MaterialContentService materialContentService;

    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<CommonResponse<List<MaterialContentResponse>>> findByName(@RequestBody MaterialContentRequest materialContent) {
        List<MaterialContentResponse> save = materialContentService.save(materialContent);

        CommonResponse<List<MaterialContentResponse>> commonResponse = CommonResponse.<List<MaterialContentResponse>>builder()
                .errors(null)
                .data(save)
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping(
            value = "/{name}",
            produces = "application/json"
    )
    public ResponseEntity<CommonResponse<List<MaterialContentResponse>>> findByName(@PathVariable String name) {
        List<MaterialContentResponse> content = materialContentService.getContent(name);

        CommonResponse<List<MaterialContentResponse>> commonResponse = CommonResponse.<List<MaterialContentResponse>>builder()
                .errors(null)
                .data(content)
                .build();

        return ResponseEntity.ok(commonResponse);
    }
}

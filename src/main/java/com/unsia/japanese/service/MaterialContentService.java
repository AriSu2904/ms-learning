package com.unsia.japanese.service;

import com.unsia.japanese.dto.request.MaterialContentRequest;
import com.unsia.japanese.dto.response.MaterialContentResponse;

import java.util.List;

public interface MaterialContentService {
    List<MaterialContentResponse> save(MaterialContentRequest materialContent);
    List<MaterialContentResponse> getContent(String name);
}

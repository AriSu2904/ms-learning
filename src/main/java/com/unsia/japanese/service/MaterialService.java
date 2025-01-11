package com.unsia.japanese.service;

import com.unsia.japanese.dto.request.MaterialRequest;
import com.unsia.japanese.dto.response.MaterialResponse;
import com.unsia.japanese.entity.Material;

import java.util.List;

public interface MaterialService {
    MaterialResponse create(MaterialRequest material);
    List<MaterialResponse> getAll();
    Material findByName(String name);
}

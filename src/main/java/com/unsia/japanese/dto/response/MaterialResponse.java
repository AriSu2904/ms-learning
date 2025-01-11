package com.unsia.japanese.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialResponse {
    private String name;
    private String description;
    private Integer order;
    private String createdBy;
    private LocalDateTime createdAt;
}

package com.unsia.japanese.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialContentResponse {
    private String id;
    private String  createdBy;
    private LocalDateTime createdAt;
    private MaterialResponse materialParent;
    private String name;
    private String description;
    private Integer position;
}

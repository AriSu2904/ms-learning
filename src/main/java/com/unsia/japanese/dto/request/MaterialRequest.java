package com.unsia.japanese.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequest {
    private String name;
    private String description;
    private Integer order;
}

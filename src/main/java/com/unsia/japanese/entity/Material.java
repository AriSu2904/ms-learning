package com.unsia.japanese.entity;

import com.unsia.japanese.dto.response.MaterialResponse;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_material")
public class Material extends Auditable<String> {
    @Column(nullable = false, unique = true)
    private String name;
    private String original;
    private String description;
    @Column(name = "display_order")
    private Integer order;
    public MaterialResponse toResponse() {
        return MaterialResponse.builder()
            .name(this.name)
            .original(this.original)
            .description(this.description)
            .order(this.order)
            .createdBy(this.getCreatedBy())
            .createdAt(this.getCreationDate())
            .build();
    }
}

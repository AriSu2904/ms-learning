package com.unsia.japanese.entity;

import com.unsia.japanese.dto.response.MaterialContentResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_material_content")
public class MaterialContent extends Auditable<String> {
    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material materialId;
    private String name;
    private String imageUri;
    private String description;
    private Integer position;

    public MaterialContentResponse toResponse() {
        return MaterialContentResponse.builder()
                .id(this.getId())
            .name(this.name)
            .description(this.description)
            .position(this.position)
                .materialParent(this.materialId.toResponse())
            .imageUri(this.imageUri)
            .createdBy(this.getCreatedBy())
            .createdAt(this.getCreationDate())
            .build();
    }
}

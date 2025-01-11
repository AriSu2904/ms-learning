package com.unsia.japanese.entity;

import com.unsia.japanese.dto.response.CharacterResponse;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_character")
public class Character extends Auditable<String> {

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;
    @Column(nullable = false)
    private String character;
    @Column(nullable = false)
    private String romaji;
    private Integer stroke;
    private String mean;
    @Column(nullable = false)
    private Integer level;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "audio_id")
    private Audio audio;
    @Column(name = "display_order")
    private Integer order;

    public CharacterResponse toResponse() {
        return CharacterResponse.builder()
                .parent(this.material.toResponse())
                .character(this.character)
                .romaji(this.romaji)
                .stroke(this.stroke)
                .mean(this.mean)
                .level(this.level)
                .image(this.image.toResponse())
                .audio(this.audio.toResponse())
                .order(this.order)
                .build();
    }
}

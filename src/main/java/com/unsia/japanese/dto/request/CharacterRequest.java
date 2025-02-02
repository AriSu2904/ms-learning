package com.unsia.japanese.dto.request;

import com.unsia.japanese.entity.Audio;
import com.unsia.japanese.entity.Image;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CharacterRequest {
    private String parentName;
    private String character;
    private String romaji;
    private String section;
    private Integer stroke;
    private String mean;
    private Integer level;
    private Integer order;
}

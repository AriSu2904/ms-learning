package com.unsia.japanese.dto.response;

import com.unsia.japanese.entity.Audio;
import com.unsia.japanese.entity.Image;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CharacterResponse {
    private MaterialResponse parent;
    private String character;
    private String romaji;
    private Integer stroke;
    private String mean;
    private Integer level;
   private String imgUri;
   private String audioUri;
    private Integer order;
}

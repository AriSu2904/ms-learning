package com.unsia.japanese.entity;

import com.unsia.japanese.dto.response.FileResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_file")
@Inheritance(strategy = InheritanceType.JOINED)
public class File extends Auditable<String> {
    private String name;
    @Column(name = "content_type")
    private String contentType;
    private String path;
    private Long size;

    public FileResponse toResponse() {
        return FileResponse.builder()
                .id(this.getId())
                .filename(this.getName())
                .url(this.getPath())
                .build();
    }
}

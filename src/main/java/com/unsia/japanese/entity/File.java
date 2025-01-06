package com.unsia.japanese.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
}

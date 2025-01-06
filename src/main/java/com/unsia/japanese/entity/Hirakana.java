package com.unsia.japanese.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_hirakana")
public class Hirakana extends Auditable<String> {
    private String name;
    private String type;
    private String team;
    @OneToOne
    private Image image;
    @OneToOne
    private Audio audio;
}

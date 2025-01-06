package com.unsia.japanese.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "m_audio")
public class Audio extends File {
    @OneToOne
    @JoinColumn(name = "kana_id", unique = true)
    private Hirakana hirakana;
}


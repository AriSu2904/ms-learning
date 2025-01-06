package com.unsia.japanese.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HirakanaResponse {
    private String id;
    private String name;
    private String type;
    private String team;
    private FileResponse image;
    private FileResponse audio;
}

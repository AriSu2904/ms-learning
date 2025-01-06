package com.unsia.japanese.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HirakanaRequest {
    private String name;
    private String type;
    private String team;
}

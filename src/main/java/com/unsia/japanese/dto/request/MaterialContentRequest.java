package com.unsia.japanese.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialContentRequest {
    private String materialParent;
    private boolean requiredLetters;
    private boolean requiredQuizzes;
    private boolean requiredEasyLearn;
    private boolean requiredTest;
}

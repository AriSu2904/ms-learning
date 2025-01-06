package com.unsia.japanese.controller;

import com.unsia.japanese.dto.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommonResponse<?>> responseException(ResponseStatusException e) {
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .errors(e.getReason())
                .data(null)
                .build();

        return ResponseEntity.status(e.getStatusCode()).body(commonResponse);
    }
}

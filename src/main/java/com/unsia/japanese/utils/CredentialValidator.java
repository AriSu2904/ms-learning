package com.unsia.japanese.utils;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

public class CredentialValidator {
    public static void isValidAuthor(Authentication authentication) {
        var authorities = authentication.getAuthorities();
        if (authorities.stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to perform this action");
        }
    }
}

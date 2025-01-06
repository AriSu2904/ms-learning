package com.unsia.japanese.service.Impl;

import com.unsia.japanese.service.JwtService;
import com.unsia.japanese.utils.KeyLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.PublicKey;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final PublicKey publicKey;

    public JwtServiceImpl(){
        try {
            this.publicKey = KeyLoader.getPublicPath("src/main/resources/public_key.pem");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String extractStudentId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token) {
        final String studentId = extractStudentId(token);
        return studentId != null && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        try {
            log.info("Extracting all claims from token with publicKey: {}", publicKey != null);

            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | ExpiredJwtException exception) {
            log.error("Error extracting all claims: {}", exception.getMessage());

            throw new RuntimeException(exception);
        }
    }

}

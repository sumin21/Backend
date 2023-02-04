package com.backend.doyouhave.jwt;


import com.backend.doyouhave.exception.ExceptionCode;
import com.backend.doyouhave.exception.JwtException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
@PropertySource("classpath:env.properties")
public class JwtTokenProvider {

    @Value("${auth.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private String createToken(final long payload, final String jwtSecret, final int jwtExpirationInMs) {
        return Jwts.builder()
                .setSubject(String.valueOf(payload))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .compact();
    }

    public String createAccessToken(final long payload) {
        return createToken(payload, jwtSecret, jwtExpirationInMs);
    }

    public Long getAccessTokenPayload(String accessToken) {
        try {
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(accessToken)
                    .getBody();

            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtException();
        }
    }

    public ExceptionCode validateToken(String accessToken) {
        try {
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(accessToken);
            if(!claims.getBody().getExpiration().before(new Date()) == false) {
                return ExceptionCode.FAIL_AUTHENTICATION;
            }
            return null;
        } catch (ExpiredJwtException e){
            return ExceptionCode.TOKEN_EXPIRED;
        } catch (Exception e) {
            return ExceptionCode.FAIL_AUTHENTICATION;
        }
    }
}

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

    @Value("${auth.jwtExpiration.accessToken}")
    private int jwtExpirationAccessToken;

    @Value("${auth.jwtExpiration.refreshToken}")
    private int jwtExpirationRefreshToken;

    private String createToken(final long payload, final String jwtSecret, final int jwtExpirationAccessToken) {
        return Jwts.builder()
                .setSubject(String.valueOf(payload))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationAccessToken))
                .compact();
    }

    public String createAccessToken(final long payload) {
        return createToken(payload, jwtSecret, jwtExpirationAccessToken);
    }

    public String createRefreshToken(final long payload) {
        return createToken(payload, jwtSecret, jwtExpirationRefreshToken);
    }

    public Long getJwtTokenPayload(String token) {
        try {
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtException();
        }
    }

    public ExceptionCode validateToken(String token) {
        try {
            var claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            if(claims.getBody().getExpiration().before(new Date())) {
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

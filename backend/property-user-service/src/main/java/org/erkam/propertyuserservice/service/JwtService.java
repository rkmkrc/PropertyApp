package org.erkam.propertyuserservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyuserservice.constants.LogMessage;
import org.erkam.propertyuserservice.constants.enums.MessageStatus;
import org.erkam.propertyuserservice.exception.jwt.JwtException;
import org.erkam.propertyuserservice.exception.jwt.JwtExceptionMessage;
import org.erkam.propertyuserservice.model.User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    private static final String SECRET_KEY = "5e73029a38e042cec717bcdb9d9851fcf2c81674af5df9a90ebde902c401d35b";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            log.error(LogMessage.generate(MessageStatus.NEG, JwtExceptionMessage.JWT_TOKEN_IS_EXPIRED));
            throw new JwtException.ExpiredJwtTokenException(JwtExceptionMessage.JWT_TOKEN_IS_EXPIRED);
        } catch (UnsupportedJwtException ex) {
            log.error(LogMessage.generate(MessageStatus.NEG, JwtExceptionMessage.JWT_TOKEN_IS_UNSUPPORTED));
            throw new JwtException.UnsupportedJwtTokenException(JwtExceptionMessage.JWT_TOKEN_IS_UNSUPPORTED);
        } catch (MalformedJwtException ex) {
            log.error(LogMessage.generate(MessageStatus.NEG, JwtExceptionMessage.JWT_TOKEN_IS_MALFORMED));
            throw new JwtException.MalformedJwtTokenException(JwtExceptionMessage.JWT_TOKEN_IS_MALFORMED);
        } catch (SignatureException ex) {
            log.error(LogMessage.generate(MessageStatus.NEG, JwtExceptionMessage.JWT_TOKEN_SIGNATURE_IS_INVALID));
            throw new JwtException.InvalidJwtTokenException(JwtExceptionMessage.JWT_TOKEN_SIGNATURE_IS_INVALID);
        } catch (IllegalArgumentException ex) {
            log.error(LogMessage.generate(MessageStatus.NEG, JwtExceptionMessage.JWT_TOKEN_IS_INVALID));
            throw new JwtException.InvalidJwtTokenException(JwtExceptionMessage.JWT_TOKEN_IS_INVALID);
        }
    }

    public Boolean isTokenValid(String token, User userDetails) {
        String username = this.extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpireDate(token).before(new Date());
    }

    private Date extractExpireDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 minutes expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

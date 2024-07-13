package org.erkam.propertylistingservice.exception.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtException extends AuthenticationException {
    public JwtException(String message) {
        super(message);
    }

    public static class InvalidJwtTokenException extends JwtException {
        public InvalidJwtTokenException(String message) {
            super(message);
        }
    }

    public static class ExpiredJwtTokenException extends JwtException {
        public ExpiredJwtTokenException(String message) {
            super(message);
        }
    }

    public static class MalformedJwtTokenException extends JwtException {
        public MalformedJwtTokenException(String message) {
            super(message);
        }
    }

    public static class UnsupportedJwtTokenException extends JwtException {
        public UnsupportedJwtTokenException(String message) {
            super(message);
        }
    }
}
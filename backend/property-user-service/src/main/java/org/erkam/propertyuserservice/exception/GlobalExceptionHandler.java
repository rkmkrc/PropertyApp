package org.erkam.propertyuserservice.exception;

import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.error.ErrorDetails;
import org.erkam.propertyuserservice.exception.jwt.JwtException;
import org.erkam.propertyuserservice.exception.jwt.JwtExceptionMessage;
import org.erkam.propertyuserservice.exception.user.UserException;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.UserAlreadyExistException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserException.UserAlreadyExistException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.PaymentFailedException.class)
    public ResponseEntity<ErrorDetails> handlePaymentFailedException(UserException.PaymentFailedException exception) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), UserExceptionMessage.PAYMENT_FAILED);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserException.UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserException.UserNotFoundException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.NoDataOnDatabaseException.class)
    public ResponseEntity<?> handleNoDataOnDatabaseException(UserException.NoDataOnDatabaseException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.ListingCouldNotCreatedException.class)
    public ResponseEntity<?> listingCouldNotCreatedException(UserException.ListingCouldNotCreatedException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtException.InvalidJwtTokenException.class)
    public ResponseEntity<ErrorDetails> handleInvalidJwtTokenException(JwtException.InvalidJwtTokenException exception) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), JwtExceptionMessage.JWT_TOKEN_IS_INVALID);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.ExpiredJwtTokenException.class)
    public ResponseEntity<ErrorDetails> handleExpiredJwtTokenException(JwtException.ExpiredJwtTokenException exception) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), JwtExceptionMessage.JWT_TOKEN_IS_EXPIRED);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.MalformedJwtTokenException.class)
    public ResponseEntity<ErrorDetails> handleMalformedJwtTokenException(JwtException.MalformedJwtTokenException exception) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), JwtExceptionMessage.JWT_TOKEN_IS_MALFORMED);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.UnsupportedJwtTokenException.class)
    public ResponseEntity<ErrorDetails> handleUnsupportedJwtTokenException(JwtException.UnsupportedJwtTokenException exception) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), JwtExceptionMessage.JWT_TOKEN_IS_UNSUPPORTED);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> handleFeignStatusException(FeignException exception, WebRequest request) {
        String simplifiedMessage = extractSimplifiedMessage(exception.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), simplifiedMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractSimplifiedMessage(String message) {
        if (message.contains("Duplicate listing found")) {
            return "Duplicate listing found.";
        }
        return message;
    }
}

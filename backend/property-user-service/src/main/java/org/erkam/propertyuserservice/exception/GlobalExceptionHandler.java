package org.erkam.propertyuserservice.exception;

import feign.FeignException;
import org.erkam.propertyuserservice.error.ErrorDetails;
import org.erkam.propertyuserservice.exception.jwt.JwtException;
import org.erkam.propertyuserservice.exception.jwt.JwtExceptionMessage;
import org.erkam.propertyuserservice.exception.listing.ListingException;
import org.erkam.propertyuserservice.exception.product.PackageException;
import org.erkam.propertyuserservice.exception.product.PackageExceptionMessage;
import org.erkam.propertyuserservice.exception.user.UserException;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // CLASS: User
    @ExceptionHandler(UserException.UserAlreadyExistException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserException.UserAlreadyExistException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.UserHasNotAnyListingsException.class)
    public ResponseEntity<?> handleUserHasNotAnyPassiveListingsException(UserException.UserHasNotAnyListingsException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.UserHasNotAnyPackagesException.class)
    public ResponseEntity<?> handleUserHasNotAnyPackagesException(UserException.UserHasNotAnyPackagesException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.ListingCouldNotUpdatedException.class)
    public ResponseEntity<?> handleListingCouldNotUpdatedException(UserException.ListingCouldNotUpdatedException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
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

    @ExceptionHandler(UserException.UserHasNotAnyPassiveListingsException.class)
    public ResponseEntity<?> handleUserHasNotAnyPassiveListingsException(UserException.UserHasNotAnyPassiveListingsException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.ListingCouldNotCreatedException.class)
    public ResponseEntity<?> handleListingCouldNotCreatedException(UserException.ListingCouldNotCreatedException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserException.ListingCouldNotDeletedException.class)
    public ResponseEntity<?> handleListingCouldNotDeletedException(UserException.ListingCouldNotDeletedException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.ListingStatusCouldNotUpdatedException.class)
    public ResponseEntity<?> handleListingStatusCouldNotUpdatedException(UserException.ListingStatusCouldNotUpdatedException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.UserIsNotEligibleToAddListing.class)
    public ResponseEntity<?> handleUserIsNotEligibleToAddListing(UserException.UserIsNotEligibleToAddListing exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    // CLASS: END - UserException
    // CLASS: JwtException
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
    // CLASS: END - JwtException
    // CLASS: PackageException
    @ExceptionHandler(PackageException.InvalidTypeOfPackageException.class)
    public ResponseEntity<ErrorDetails> handleInvalidPackageTypeException(PackageException.InvalidTypeOfPackageException exception) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), PackageExceptionMessage.INVALID_TYPE_OF_PACKAGE);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    // CLASS: END - PackageException
    // CLASS: ListingException
    @ExceptionHandler(ListingException.InvalidTypeOfStatusException.class)
    public ResponseEntity<ErrorDetails> handleInvalidTypeOfStatusException(ListingException.InvalidTypeOfStatusException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    // CLASS: END - ListingException
    // CLASS: FeignException
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> handleFeignStatusException(FeignException exception, WebRequest request) {
        String simplifiedMessage = extractSimplifiedMessage(exception.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), simplifiedMessage, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // CLASS: END - FeignException
    // CLASS: Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // CLASS: END - Exception

    // TODO: Handle extractSimplifiedMessage() more elegant later
    private String extractSimplifiedMessage(String message) {
        if (message.contains("Duplicate listing found")) {
            return "Duplicate listing found.";
        } else if (message.contains("No listing found")) {
            return "No listings found.";
        } else if (message.contains("No passive listings found")) {
            return "No passive listings found.";
        } else if (message.contains("No active listings found")) {
            return "No active listings found.";
        } else if (message.contains("Listing not found or you don't have permission")) {
            return "Listing not found or you don't have permission";
        }
        return message;
    }
}

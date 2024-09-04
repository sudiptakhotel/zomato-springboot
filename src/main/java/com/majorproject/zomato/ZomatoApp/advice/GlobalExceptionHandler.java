package com.majorproject.zomato.ZomatoApp.advice;

import com.majorproject.zomato.ZomatoApp.exception.*;
import io.jsonwebtoken.JwtException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError apiError) {

        return new ResponseEntity<>(new ApiResponse<>(apiError) , apiError.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiError , HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CustomerNotMatchedException.class)
    public ResponseEntity<ApiError> handleCustomerNotMatchedException(CustomerNotMatchedException exception) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_ACCEPTABLE)
                .error(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiError , HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(RestaurantNotMatchedException.class)
    public ResponseEntity<ApiError> handleRestaurantNotMatchedException(RestaurantNotMatchedException exception) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_ACCEPTABLE)
                .error(exception.getMessage())
                .build();

        return new ResponseEntity<>(apiError , HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        List<String> subError = exception.
                getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(exception.getMessage())
                .subError(subError)
                .build();

        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(CartHasNoItemException.class)
    public ResponseEntity<ApiResponse<?>> handleCartHasNoItemException(CartHasNoItemException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(CartInvalidException.class)
    public ResponseEntity<ApiResponse<?>> handleCartInvalidException(CartInvalidException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(DuplicateCartFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateCartFoundException(DuplicateCartFoundException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(DuplicatePromoInsertionException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicatePromoInsertionException(DuplicatePromoInsertionException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidOrderStatusException(InvalidOrderStatusException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidPartnerException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidPartnerException(InvalidPartnerException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(OSRMException.class)
    public ResponseEntity<ApiResponse<?>> handleOSRMException(OSRMException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(PartnerNotAvailableException.class)
    public ResponseEntity<ApiResponse<?>> handlePartnerNotAvailableException(PartnerNotAvailableException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(RestaurantClosedException.class)
    public ResponseEntity<ApiResponse<?>> handleRestaurantClosedException(RestaurantClosedException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeConflictException(RuntimeConflictException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(WrongCustomerOtpException.class)
    public ResponseEntity<ApiResponse<?>> handleWrongCustomerOtpException(WrongCustomerOtpException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(WrongRestaurantOtpException.class)
    public ResponseEntity<ApiResponse<?>> handleWrongRestaurantOtpException(WrongRestaurantOtpException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }
}

package com.example.interviewtracker.common;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFound(ResourceNotFoundException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoResourceFound(NoResourceFoundException ex) {
        return ApiResponse.error("요청하신 리소스를 찾을 수 없습니다: " + ex.getResourcePath());
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handlePropertyReferenceException(PropertyReferenceException ex) {
        return ApiResponse.error("잘못된 정렬 기준 필드입니다: " + ex.getPropertyName());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
        return ApiResponse.error("잘못된 파라미터가 포함되어 있습니다 (예: 존재하지 않는 정렬 필드 등).");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ApiResponse.error(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGenericException(Exception ex) {
        return ApiResponse.error("서버 내부 오류가 발생했습니다: " + ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ApiResponse.error("잘못된 요청 데이터 형식입니다 (예: JSON 문법 오류 또는 존재하지 않는 Enum 값).");
    }
}

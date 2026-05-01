package com.example.interviewtracker.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "성공적으로 처리되었습니다.", data);
    }
    
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, "성공적으로 처리되었습니다.", null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}

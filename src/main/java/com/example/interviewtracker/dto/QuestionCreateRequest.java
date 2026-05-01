package com.example.interviewtracker.dto;

import com.example.interviewtracker.domain.Category;
import com.example.interviewtracker.domain.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionCreateRequest {

    @Schema(description = "질문 제목", example = "JVM이란 무엇인가요?")
    @NotBlank(message = "질문 제목은 필수입니다.")
    private String title;

    @Schema(description = "질문 답변", example = "JVM은 Java Virtual Machine의 약자로, 자바 바이트코드를 실행하는 가상 머신입니다.")
    @NotBlank(message = "질문 답변은 필수입니다.")
    private String answer;

    @Schema(description = "출처", example = "backend-interview-question GitHub")
    private String source;

    @Schema(description = "카테고리", example = "JAVA")
    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    @Schema(description = "이해도 상태", example = "UNCERTAIN")
    @NotNull(message = "상태는 필수입니다.")
    private Status status;
}

package com.example.interviewtracker.dto;

import com.example.interviewtracker.domain.Category;
import com.example.interviewtracker.domain.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionUpdateRequest {

    @NotBlank(message = "질문 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "질문 답변은 필수입니다.")
    private String answer;

    private String source;

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    @NotNull(message = "상태는 필수입니다.")
    private Status status;
}

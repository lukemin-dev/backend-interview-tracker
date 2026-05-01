package com.example.interviewtracker.dto;

import com.example.interviewtracker.domain.Category;
import com.example.interviewtracker.domain.Question;
import com.example.interviewtracker.domain.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuestionResponse {
    private Long id;
    private String title;
    private String answer;
    private String source;
    private Category category;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuestionResponse(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.answer = question.getAnswer();
        this.source = question.getSource();
        this.category = question.getCategory();
        this.status = question.getStatus();
        this.createdAt = question.getCreatedAt();
        this.updatedAt = question.getUpdatedAt();
    }
}

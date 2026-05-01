package com.example.interviewtracker.controller;

import com.example.interviewtracker.common.ApiResponse;
import com.example.interviewtracker.domain.Category;
import com.example.interviewtracker.domain.Status;
import com.example.interviewtracker.dto.QuestionCreateRequest;
import com.example.interviewtracker.dto.QuestionResponse;
import com.example.interviewtracker.dto.QuestionUpdateRequest;
import com.example.interviewtracker.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ApiResponse<Long> createQuestion(@Valid @RequestBody QuestionCreateRequest request) {
        Long id = questionService.createQuestion(request);
        return ApiResponse.success(id);
    }

    @GetMapping("/{id}")
    public ApiResponse<QuestionResponse> getQuestion(@PathVariable("id") Long id) {
        QuestionResponse response = questionService.getQuestion(id);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<Page<QuestionResponse>> searchQuestions(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort) {

        String[] sortParams = sort.split(",");
        String property = sortParams[0];
        org.springframework.data.domain.Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") 
                ? org.springframework.data.domain.Sort.Direction.DESC 
                : org.springframework.data.domain.Sort.Direction.ASC;
        
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(direction, property));
        Page<QuestionResponse> responses = questionService.searchQuestions(keyword, category, status, pageable);
        return ApiResponse.success(responses);
    }

    @PutMapping("/{id}")
    public ApiResponse<QuestionResponse> updateQuestion(
            @PathVariable("id") Long id,
            @Valid @RequestBody QuestionUpdateRequest request) {
        QuestionResponse response = questionService.updateQuestion(id, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteQuestion(@PathVariable("id") Long id) {
        questionService.deleteQuestion(id);
        return ApiResponse.success();
    }
}

package com.example.interviewtracker.service;

import com.example.interviewtracker.common.ResourceNotFoundException;
import com.example.interviewtracker.domain.Category;
import com.example.interviewtracker.domain.Question;
import com.example.interviewtracker.domain.Status;
import com.example.interviewtracker.dto.QuestionCreateRequest;
import com.example.interviewtracker.dto.QuestionResponse;
import com.example.interviewtracker.dto.QuestionUpdateRequest;
import com.example.interviewtracker.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public Long createQuestion(QuestionCreateRequest request) {
        Question question = Question.builder()
                .title(request.getTitle())
                .answer(request.getAnswer())
                .source(request.getSource())
                .category(request.getCategory())
                .status(request.getStatus())
                .build();
        return questionRepository.save(question).getId();
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestion(Long id) {
        Question question = findQuestionById(id);
        return new QuestionResponse(question);
    }

    @Transactional(readOnly = true)
    public Page<QuestionResponse> searchQuestions(String keyword, Category category, Status status, Pageable pageable) {
        Page<Question> questions = questionRepository.searchQuestions(keyword, category, status, pageable);
        return questions.map(QuestionResponse::new);
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionUpdateRequest request) {
        Question question = findQuestionById(id);
        question.update(
                request.getTitle(),
                request.getAnswer(),
                request.getSource(),
                request.getCategory(),
                request.getStatus()
        );
        return new QuestionResponse(question);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        Question question = findQuestionById(id);
        questionRepository.delete(question);
    }

    private Question findQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 질문을 찾을 수 없습니다. ID: " + id));
    }
}

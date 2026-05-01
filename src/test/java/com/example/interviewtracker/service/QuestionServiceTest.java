package com.example.interviewtracker.service;

import com.example.interviewtracker.domain.Category;
import com.example.interviewtracker.domain.Question;
import com.example.interviewtracker.domain.Status;
import com.example.interviewtracker.dto.QuestionCreateRequest;
import com.example.interviewtracker.dto.QuestionResponse;
import com.example.interviewtracker.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    @DisplayName("질문 생성 테스트")
    void createQuestionTest() {
        // given
        QuestionCreateRequest request = new QuestionCreateRequest();
        request.setTitle("Test Title");
        request.setAnswer("Test Answer");
        request.setCategory(Category.JAVA);
        request.setStatus(Status.UNKNOWN);

        Question question = Question.builder()
                .title(request.getTitle())
                .answer(request.getAnswer())
                .category(request.getCategory())
                .status(request.getStatus())
                .build();
        ReflectionTestUtils.setField(question, "id", 1L);

        when(questionRepository.save(any(Question.class))).thenReturn(question);

        // when
        Long id = questionService.createQuestion(request);

        // then
        assertEquals(1L, id);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    @DisplayName("질문 조회 테스트")
    void getQuestionTest() {
        // given
        Question question = Question.builder()
                .title("Test Title")
                .answer("Test Answer")
                .category(Category.JAVA)
                .status(Status.UNKNOWN)
                .build();
        ReflectionTestUtils.setField(question, "id", 1L);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // when
        QuestionResponse response = questionService.getQuestion(1L);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Title", response.getTitle());
    }
}

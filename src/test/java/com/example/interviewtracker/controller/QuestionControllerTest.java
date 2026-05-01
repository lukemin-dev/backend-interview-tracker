package com.example.interviewtracker.controller;

import com.example.interviewtracker.domain.Category;
import com.example.interviewtracker.domain.Question;
import com.example.interviewtracker.domain.Status;
import com.example.interviewtracker.dto.QuestionCreateRequest;
import com.example.interviewtracker.dto.QuestionResponse;
import com.example.interviewtracker.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuestionService questionService;

    @Test
    @DisplayName("질문 등록 API 테스트")
    void createQuestionApiTest() throws Exception {
        // given
        QuestionCreateRequest request = new QuestionCreateRequest();
        request.setTitle("Test Title");
        request.setAnswer("Test Answer");
        request.setCategory(Category.JAVA);
        request.setStatus(Status.UNKNOWN);

        when(questionService.createQuestion(any(QuestionCreateRequest.class))).thenReturn(1L);

        // when & then
        mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    @DisplayName("질문 등록 실패 (Validation) 테스트")
    void createQuestionApiValidationFailTest() throws Exception {
        // given
        QuestionCreateRequest request = new QuestionCreateRequest();
        // title, answer 등 필수값이 없음

        // when & then
        mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("질문 조회 API 테스트")
    void getQuestionApiTest() throws Exception {
        // given
        Question question = Question.builder()
                .title("Test Title")
                .answer("Test Answer")
                .category(Category.JAVA)
                .status(Status.UNKNOWN)
                .build();
        ReflectionTestUtils.setField(question, "id", 1L);
        QuestionResponse response = new QuestionResponse(question);

        when(questionService.getQuestion(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/questions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Test Title"));
    }
}

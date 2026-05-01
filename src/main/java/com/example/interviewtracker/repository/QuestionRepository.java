package com.example.interviewtracker.repository;

import com.example.interviewtracker.domain.Category;
import com.example.interviewtracker.domain.Question;
import com.example.interviewtracker.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    @Query("SELECT q FROM Question q WHERE " +
           "(:keyword IS NULL OR q.title LIKE %:keyword% OR q.answer LIKE %:keyword%) " +
           "AND (:category IS NULL OR q.category = :category) " +
           "AND (:status IS NULL OR q.status = :status)")
    Page<Question> searchQuestions(@Param("keyword") String keyword, 
                                   @Param("category") Category category, 
                                   @Param("status") Status status, 
                                   Pageable pageable);
}

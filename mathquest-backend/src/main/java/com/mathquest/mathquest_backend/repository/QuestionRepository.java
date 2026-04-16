package com.mathquest.mathquest_backend.repository;

import com.mathquest.mathquest_backend.domain.Question;
import com.mathquest.mathquest_backend.domain.QuestionDificulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDifficulty(QuestionDificulty nivelDeDificuldade);
    List<Question> findByIdNotIn(List<Long> idsUsados);
    List<Question> findByDifficultyAndIdNotIn(QuestionDificulty nivelDeDificuldade, List<Long> idsUsados);
}

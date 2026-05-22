package com.mathquest.mathquest_backend.repository;

import com.mathquest.mathquest_backend.domain.Question;
import com.mathquest.mathquest_backend.domain.QuestionDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDifficulty(QuestionDifficulty nivelDeDificuldade);
    List<Question> findByIdNotIn(List<Long> idsUsados);
    List<Question> findByDifficultyAndIdNotIn(QuestionDifficulty nivelDeDificuldade, List<Long> idsUsados);
}

package com.mathquest.mathquest_backend.repository;

import com.mathquest.mathquest_backend.domain.Game;
import com.mathquest.mathquest_backend.domain.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByStatus(GameStatus gameStatus);
}

package com.mathquest.mathquest_backend.repository;

import com.mathquest.mathquest_backend.domain.BoardSquare;
import com.mathquest.mathquest_backend.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardSquareRepository extends JpaRepository <BoardSquare, Long>{
    Optional<BoardSquare> findByNumeroDeCasasAndGame(int numeroCasa, Game game);
    List<BoardSquare> findByGame(Game game);
}

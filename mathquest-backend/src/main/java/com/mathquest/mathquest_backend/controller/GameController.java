package com.mathquest.mathquest_backend.controller;

import com.mathquest.mathquest_backend.domain.BoardSquare;
import com.mathquest.mathquest_backend.domain.Game;
import com.mathquest.mathquest_backend.domain.Player;
import com.mathquest.mathquest_backend.dto.AnswerDTO;
import com.mathquest.mathquest_backend.dto.BoardSquareDTO;
import com.mathquest.mathquest_backend.dto.GameStateDTO;
import com.mathquest.mathquest_backend.repository.BoardSquareRepository;
import com.mathquest.mathquest_backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/game")
public class GameController {
    GameService gameService;
    BoardSquareRepository boardSquareRepository;
    @Autowired
    public GameController(GameService gameService, BoardSquareRepository boardSquareRepository ) {
        this.gameService = gameService;
        this.boardSquareRepository = boardSquareRepository;
    }
    @PostMapping("/start")
    public GameStateDTO criarPartida(@RequestBody List<String> nomesJogadores) {
        return gameService.criarPartida(nomesJogadores);
    }
    @PutMapping("/rolar/{gameId}") //@PutMapping
    public GameStateDTO rolarDado(@PathVariable Long gameId){
        return gameService.rolarDado(gameId);
    }
    @PutMapping("/responder") //@PutMapping
    public GameStateDTO responderQuestão(@RequestBody AnswerDTO answerDTO){
        return gameService.responderQuestao(answerDTO);
    }
    @PutMapping("/usar-dica/{gameId}") //@PutMapping
    public GameStateDTO usarDica(@PathVariable Long gameId, @RequestParam Long questaoId){
        return gameService.usarDica(gameId, questaoId);
    }
    // Envia o tipo de casa de cada desafio (Tipo: BOOLEANA, FUNÇÕES, etc.) para o front-end
    @GetMapping("/buscar-tabuleiro")
    public List<BoardSquareDTO> buscarTabuleiro(@RequestParam Long gameId){
        return gameService.buscarTabuleiro(gameId);
    }

}

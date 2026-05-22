package com.mathquest.mathquest_backend.controller;

import com.mathquest.mathquest_backend.config.WebSocketEventListener;
import com.mathquest.mathquest_backend.dto.AnswerDTO;
import com.mathquest.mathquest_backend.dto.DicaRequestDTO;
import com.mathquest.mathquest_backend.dto.GameStateDTO;
import com.mathquest.mathquest_backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GameWebSocketController {
    private GameService gameService;
    private SimpMessagingTemplate simpMessagingTemplate;
    private WebSocketEventListener webSocketEventListener;

    @Autowired
    public GameWebSocketController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate, WebSocketEventListener webSocketEventListener) {
        this.gameService = gameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.webSocketEventListener = webSocketEventListener;
    }
    @MessageMapping("/game/rolar")
    public GameStateDTO rolarDado(@Payload Long gameId){
        GameStateDTO gameStateDTO = gameService.rolarDado(gameId);
        simpMessagingTemplate.convertAndSend("/topic/game/"+ gameId, gameStateDTO);
        return gameStateDTO;
    }
    @MessageMapping("/game/start")
    @SendTo("/topic/game/novo")
    public GameStateDTO criarPartida(@Payload List<String> nomesJogadores, SimpMessageHeaderAccessor headerAccessor){
        GameStateDTO gameStateDTO = gameService.criarPartida(nomesJogadores);
        // Registra a sessão para limpeza posterior
        String sessionId = headerAccessor.getSessionId();
        webSocketEventListener.registrarSessao(sessionId, gameStateDTO.getGameId());
        return gameStateDTO;
    }
    @MessageMapping("/game/responder")
    public GameStateDTO responderQuestao(@Payload AnswerDTO answerDTO){
        GameStateDTO gameStateDTO = gameService.responderQuestao(answerDTO);
        simpMessagingTemplate.convertAndSend("/topic/game/" + answerDTO.getGameId(), gameStateDTO);
        return gameStateDTO;
    }
    @MessageMapping("/game/usar-dica")
    public GameStateDTO usarDica(@Payload DicaRequestDTO dicaRequest) {
        GameStateDTO gameStateDTO = gameService.usarDica(dicaRequest.getGameId(), dicaRequest.getQuestaoId());
        simpMessagingTemplate.convertAndSend("/topic/game/" + dicaRequest.getGameId(), gameStateDTO);
        return gameStateDTO;
    }
}

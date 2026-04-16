package com.mathquest.mathquest_backend.config;

import com.mathquest.mathquest_backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {
    private Map<String, Long> map;
    private GameService gameService;
    @Autowired
    public WebSocketEventListener(GameService gameService) {
        this.gameService = gameService;
        this.map = new ConcurrentHashMap<>();
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event){
        String sessionId = event.getSessionId();
        Long gameId = map.get(sessionId);
        if(gameId != null){
            gameService.finalizarPartida(gameId);
            map.remove(sessionId);
        }

    }
    public void registrarSessao(String sessionId, Long gameId){
        map.put(sessionId, gameId);
    }

}

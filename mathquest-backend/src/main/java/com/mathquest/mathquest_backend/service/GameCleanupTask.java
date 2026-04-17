package com.mathquest.mathquest_backend.service;

import com.mathquest.mathquest_backend.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class GameCleanupTask {
    private GameService gameService;
    @Autowired
    public GameCleanupTask(GameService gameService) {
        this.gameService = gameService;
    }

    //Impede que partidas em andamento continuem após inatividade do jogador ou do sistema e as exclui do BD
    @Scheduled(fixedRate = 60000)
    public void limparPartidasInativas(){
        LocalDateTime limite  = LocalDateTime.now().minusMinutes(10);
        List<Game> ativas = gameService.buscarPartidasAtivas();
        for(Game game : ativas){
            if(game.getUltimaAcao() == null || game.getUltimaAcao().isBefore(limite)){
                gameService.finalizarPartida(game.getId());
            }
        }
    }

}

package com.mathquest.mathquest_backend.dto;

import com.mathquest.mathquest_backend.domain.GameStatus;
import com.mathquest.mathquest_backend.domain.SquareType;
import lombok.Data;

import java.util.List;

@Data
public class GameStateDTO {
    private Long gameId;
    private Long playerId;
    private String nomeJogadorAtual;
    private List<PlayerDTO> jogadores;
    private int posicaoAtual;
    private int pontos;
    private int dicasDisponiveis;
    private GameStatus status;
    private SquareType tipoDaCasaAtual;
    private QuestionDTO questaoAtual;
    private Integer dadoResultado;
    private String mensagem;
    private String dica;
    private boolean acertou;
    private int turnoAtual;

}

package com.mathquest.mathquest_backend.dto;

import com.mathquest.mathquest_backend.domain.GameStatus;
import com.mathquest.mathquest_backend.domain.SquareType;
import lombok.Data;

@Data
public class GameStateDTO {
    private Long gameId;
    private Long playerId;
    private String nomeJogadorAtual;
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

}

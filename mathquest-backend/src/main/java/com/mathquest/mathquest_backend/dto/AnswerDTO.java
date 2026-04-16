package com.mathquest.mathquest_backend.dto;

import lombok.Data;

@Data
public class AnswerDTO {
    private Long gameId;
    private Long questaoId;
    private int indiceResposta;
    private String respostaSelecionada;
}

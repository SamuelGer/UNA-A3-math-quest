package com.mathquest.mathquest_backend.dto;

import lombok.Data;

@Data
public class PlayerDTO {
    private String nome;
    private int pontos;
    private int posicaoAtual;
    public PlayerDTO(Long id, String nome, int pontos, int posicaoAtual) {
        this.nome = nome;
        this.pontos = pontos;
        this.posicaoAtual = posicaoAtual;
    }
}

package com.mathquest.mathquest_backend.dto;

import lombok.Data;

@Data
public class PlayerDTO {
    private String nome;
    private int pontos;
    private int posicaoAtual;
    private int dicasDisponiveis;
    public PlayerDTO(Long id, String nome, int pontos, int posicaoAtual, int dicasDisponiveis) {
        this.nome = nome;
        this.pontos = pontos;
        this.posicaoAtual = posicaoAtual;
        this.dicasDisponiveis = dicasDisponiveis;
    }
}

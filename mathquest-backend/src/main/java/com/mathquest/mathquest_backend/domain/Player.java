package com.mathquest.mathquest_backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int pontos;
    private int posicaoAtual;
    private int dicasDisponiveis;

}

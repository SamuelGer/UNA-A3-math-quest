package com.mathquest.mathquest_backend.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table (name = "partidas")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<Player> jogadores;
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    private int turnoAtual;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> questaoId;
    private LocalDateTime ultimaAcao;

}

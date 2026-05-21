package com.mathquest.mathquest_backend.java.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tabuleiro")
public class BoardSquare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numeroDeCasas;
    @Enumerated(EnumType.STRING)
    private SquareType tipo;
    @ManyToOne
    private Game game;

    
}

package com.mathquest.mathquest_backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Data
@Entity
@Table(name = "questoes")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pergunta;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> opcoes;
    private int indiceCorreto;
    @Enumerated(EnumType.STRING)
    private QuestionCategory categoria;
    private String dica;
    private String explicacao;
    @Enumerated(EnumType.STRING)
    private QuestionDificulty difficulty;

}

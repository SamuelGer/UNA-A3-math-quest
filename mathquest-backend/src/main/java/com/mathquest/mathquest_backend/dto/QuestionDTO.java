package com.mathquest.mathquest_backend.java.dto;

import com.mathquest.mathquest_backend.java.domain.QuestionCategory;
import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String pergunta;
    private List<String> opcoes;
    private QuestionCategory categoria;
    private String dica;
    private String explicacao;
    private int indiceCorreto;
}

package com.mathquest.mathquest_backend.java.dto;


import com.mathquest.mathquest_backend.java.domain.SquareType;
import lombok.Data;

@Data
public class BoardSquareDTO {
    private int numeroCasa;
    private SquareType tipo;
}

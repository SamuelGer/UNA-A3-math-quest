package com.mathquest.mathquest_backend.dto;


import com.mathquest.mathquest_backend.domain.SquareType;
import lombok.Data;

@Data
public class BoardSquareDTO {
    private int numeroCasa;
    private SquareType tipo;
}

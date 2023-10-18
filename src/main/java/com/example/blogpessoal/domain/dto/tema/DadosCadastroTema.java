package com.example.blogpessoal.domain.dto.tema;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroTema(
        @NotBlank( message = "O Atributo Descrição é obrigatório")
        String descricao
        ) {
}

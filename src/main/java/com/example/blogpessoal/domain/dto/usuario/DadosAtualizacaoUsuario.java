package com.example.blogpessoal.domain.dto.usuario;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoUsuario(
        String nome,

        String email,

        String senha,

        String imagem,

        @NotNull
        Long id,

        @NotBlank
        String token
        ) {
    public DadosAtualizacaoUsuario(Long id, String token) {
        this(null, null, null, null, id, token);
    }

    public DadosAtualizacaoUsuario(DadosBodyAtaualizacaoUsuario bodyDto, Long id, String token) {
        this(bodyDto.nome(), bodyDto.email(), bodyDto.senha(), bodyDto.imagem(), id, token);
    }
}

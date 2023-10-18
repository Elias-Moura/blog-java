package com.example.blogpessoal.domain.dto.postagem;

import com.example.blogpessoal.domain.dto.tema.DadosTema;

import java.time.LocalDateTime;

public record DadosAtualizacaoPostagem(
        Long id,
        String titulo,
        String texto,
        LocalDateTime data,
        DadosTema tema
) {
}

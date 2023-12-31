package com.example.blogpessoal.domain.dto.postagem;

import com.example.blogpessoal.domain.dto.tema.DadosAtualizacaoTema;
import com.example.blogpessoal.domain.modelos.Postagem;

import java.time.LocalDateTime;

public record DadosAtualizacaoPostagem(
        Long id,
        String titulo,
        String texto,
        LocalDateTime data,
        DadosAtualizacaoTema tema
) {
    public DadosAtualizacaoPostagem(Postagem dados) {
        this(
                dados.getId(),
                dados.getTitulo(),
                dados.getTexto(),
                dados.getData(),
                new DadosAtualizacaoTema(dados.getTema()));
    }
}

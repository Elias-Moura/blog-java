package com.example.blogpessoal.domain.dto.postagem;

import com.example.blogpessoal.domain.dto.tema.DadosListagemTema;
import com.example.blogpessoal.domain.modelos.Postagem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public record DadosListagemPostagem(
        Long id,
        String titulo,
        String texto,
        LocalDateTime data,
        @JsonIgnoreProperties("postagem")
        DadosListagemTema tema
) {
    public DadosListagemPostagem(Postagem postagem) {
        this(
                postagem.getId(),
                postagem.getTitulo(),
                postagem.getTexto(),
                postagem.getData(),
                new DadosListagemTema(postagem.getTema())
        );
    }
}

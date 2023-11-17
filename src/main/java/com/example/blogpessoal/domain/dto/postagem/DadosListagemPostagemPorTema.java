package com.example.blogpessoal.domain.dto.postagem;

import com.example.blogpessoal.domain.dto.usuario.DadosListagemUsuarioSemPosts;
import com.example.blogpessoal.domain.modelos.Postagem;

import java.time.LocalDateTime;

public record DadosListagemPostagemPorTema(
        Long id,
        String titulo,
        String texto,
        LocalDateTime data,
        DadosListagemUsuarioSemPosts usuario
) {
    public DadosListagemPostagemPorTema(Postagem dados){
        this(
                dados.getId(),
                dados.getTitulo(),
                dados.getTexto(),
                dados.getData(),
                new DadosListagemUsuarioSemPosts(dados.getUsuario())
        );
    }
}

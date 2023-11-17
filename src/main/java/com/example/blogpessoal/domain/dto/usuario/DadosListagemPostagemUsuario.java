package com.example.blogpessoal.domain.dto.usuario;

import com.example.blogpessoal.domain.modelos.Postagem;

import java.time.LocalDateTime;

public record DadosListagemPostagemUsuario(
        Long id,
        String titulo,
        String texto,
        LocalDateTime data,
        Long temaId,
        String nomeTema
) {
    public DadosListagemPostagemUsuario(Postagem dados){
        this(
                dados.getId(),
                dados.getTitulo(),
                dados.getTexto(),
                dados.getData(),
                dados.getTema().getId(),
                dados.getTema().getDescricao()
        );
    }
}

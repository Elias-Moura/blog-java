package com.example.blogpessoal.domain.dto.usuario;

import com.example.blogpessoal.domain.modelos.Usuario;

public record DadosListagemUsuarioSemPosts(
        Long id,
        String nome,
        String email,
        String imagem
) {
    public DadosListagemUsuarioSemPosts(Usuario dados) {
        this(dados.getId(), dados.getNome(), dados.getEmail(), dados.getImagem());
    }
}

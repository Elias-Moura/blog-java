package com.example.blogpessoal.domain.dto.usuario;

import com.example.blogpessoal.domain.modelos.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public record DadosListagemUsuario(
        Long id,
        String nome,
        String email,
        String imagem,
        @JsonIgnoreProperties("postagems")
        List<DadosListagemPostagemUsuario> postagems
) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getImagem(),
                usuario.getPostagens().stream().map(DadosListagemPostagemUsuario::new).toList());
    }
}

package com.example.blogpessoal.domain.dto.usuario;

import com.example.blogpessoal.domain.modelos.Postagem;
import com.example.blogpessoal.domain.modelos.Usuario;

import java.util.List;

public record DadosListagemUsuario(
        Long id,
        String nome,
        String email,
        String imagem,
        List<Postagem> postagems
) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getImagem(), usuario.getPostagems());
    }
}

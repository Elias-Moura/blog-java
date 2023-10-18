package com.example.blogpessoal.domain.dto.usuario;

import com.example.blogpessoal.domain.modelos.Usuario;
import jakarta.persistence.Column;

public record DadosCadastroUsuario(
        String nome,
        String email,
        String senha,
        String imagem) {

    public DadosCadastroUsuario(Usuario dados) {
        this(dados.getNome(), dados.getEmail(), dados.getSenha(), dados.getImagem());
    }
}

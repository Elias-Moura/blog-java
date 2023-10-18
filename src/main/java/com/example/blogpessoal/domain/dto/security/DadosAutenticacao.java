package com.example.blogpessoal.domain.dto.security;

import com.example.blogpessoal.domain.modelos.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String senha
) {
        public DadosAutenticacao(Usuario user) {
                this(user.getEmail(), user.getSenha());
        }
}


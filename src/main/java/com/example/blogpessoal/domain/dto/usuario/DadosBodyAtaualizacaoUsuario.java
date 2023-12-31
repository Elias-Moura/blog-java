package com.example.blogpessoal.domain.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosBodyAtaualizacaoUsuario(

        @NotNull
        Long id,
        @Size(min = 3, max = 255, message = "The name must be between 3 and 255 characters long.")
        String nome,
        @Email(message = "The field email must be in a valid email format.")
        @NotBlank(message = "The email field is required.")
        @Size(max = 255, message = "The email field must be shorter than 255 characters.")
        String email,
        @Size(min = 5, max = 30, message = "The password must be between 5 and 30 characters long.")
        String senha,
        @Size(max = 5000, message = "The image field must be shorter than 5000 characters.")
        String imagem
) {
}

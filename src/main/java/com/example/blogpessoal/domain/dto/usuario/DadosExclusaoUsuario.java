package com.example.blogpessoal.domain.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosExclusaoUsuario(
        @NotNull
        Long id,
        @Email(message = "The field email must be in a valid email format.")
        @NotBlank(message = "The email field is required.")
        @Size(max = 255, message = "The email field must be shorter than 255 characters.")
        String email
){

}
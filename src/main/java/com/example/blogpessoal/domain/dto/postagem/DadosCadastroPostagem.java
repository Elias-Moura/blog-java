package com.example.blogpessoal.domain.dto.postagem;

import com.example.blogpessoal.domain.modelos.Postagem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record DadosCadastroPostagem(
        @NotNull
        @Size(min = 5, max = 100, message = "O atributo deve conter no mínimo 05 caracteres e no máximo 100")
        String titulo,
        @NotNull
        @Size(min = 5, max = 100, message = "O atributo deve conter no mínimo 10 caracteres e no máximo 1000")
        String texto,
        LocalDateTime data,
        @NotNull
        Long temaId,
        @NotNull
        Long usuarioId

){
        public DadosCadastroPostagem(Postagem dados) {
                this(
                        dados.getTitulo(),
                        dados.getTexto(),
                        dados.getData(),
                        dados.getTema().getId(),
                        dados.getUsuario().getId()
                        );
        }
}

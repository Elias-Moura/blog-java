package com.example.blogpessoal.domain.dto.postagem;

import com.example.blogpessoal.domain.dto.tema.DadosTema;
import com.example.blogpessoal.domain.modelos.Postagem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record DadosCadastroPostagem(
        @NotBlank(message = "O atributo título é obrigatório")
        @Size(min = 5, max = 100, message = "O atributo deve conter no mínimo 05 caracteres e no máximo 100")
        String titulo,
        @NotBlank(message = "O atributo texto é obrigatório")
        @Size(min = 5, max = 100, message = "O atributo deve conter no mínimo 10 caracteres e no máximo 1000")
        String texto,

        LocalDateTime data,
        DadosTema tema

){
        public DadosCadastroPostagem(Postagem dados) {
                this(dados.getTitulo(), dados.getTexto(), dados.getData(), new DadosTema(dados.getTema()));
        }
}

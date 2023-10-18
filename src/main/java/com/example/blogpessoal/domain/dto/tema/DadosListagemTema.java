package com.example.blogpessoal.domain.dto.tema;

import com.example.blogpessoal.domain.modelos.Postagem;
import com.example.blogpessoal.domain.modelos.Tema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public record DadosListagemTema(Long id,
                                String descricao,
                                @JsonIgnoreProperties("tema")
                                List<Postagem> postagem) {

    public DadosListagemTema(Tema dados) {
        this(dados.getId(), dados.getDescricao(), dados.getPostagems());
    }
}

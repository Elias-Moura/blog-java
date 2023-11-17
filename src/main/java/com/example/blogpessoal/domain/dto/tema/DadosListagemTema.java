package com.example.blogpessoal.domain.dto.tema;

import com.example.blogpessoal.domain.dto.postagem.DadosListagemPostagem;
import com.example.blogpessoal.domain.dto.postagem.DadosListagemPostagemPorTema;
import com.example.blogpessoal.domain.modelos.Postagem;
import com.example.blogpessoal.domain.modelos.Tema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public record DadosListagemTema(Long id,
                                String titulo,
                                String descricao,
                                @JsonIgnoreProperties("tema")
                                List<DadosListagemPostagemPorTema> postagens) {

    public DadosListagemTema(Tema dados) {
        this(dados.getId(), dados.getTitulo(),
                dados.getDescricao(),
                dados.getPostagens().stream().map(DadosListagemPostagemPorTema::new).toList()
        );
    }
}

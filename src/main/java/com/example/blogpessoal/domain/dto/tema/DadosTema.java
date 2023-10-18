package com.example.blogpessoal.domain.dto.tema;

import com.example.blogpessoal.domain.modelos.Tema;

public record DadosTema(Long id) {
    public DadosTema(Tema dados){
        this(dados.getId());
    }
}

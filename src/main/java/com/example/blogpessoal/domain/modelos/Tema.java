package com.example.blogpessoal.domain.modelos;

import com.example.blogpessoal.domain.dto.tema.DadosAtualizacaoTema;
import com.example.blogpessoal.domain.dto.tema.DadosCadastroTema;
import com.example.blogpessoal.domain.dto.tema.DadosTema;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name= "tb_temas")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    @OneToMany(mappedBy = "tema", cascade= CascadeType.REMOVE)
    private List<Postagem> postagems;
    private Boolean ativo;

    public Tema(DadosTema tema) {
        this.id = tema.id();
    }

    public Tema(DadosCadastroTema tema) {
        this.descricao = tema.descricao();
    }

    public void atualizarInformacoes(DadosAtualizacaoTema dados) {
        this.descricao = dados.descricao();
    }

    public void excluir() {
        this.ativo = false;
    }
}

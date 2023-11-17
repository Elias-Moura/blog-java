package com.example.blogpessoal.domain.modelos;

import com.example.blogpessoal.domain.dto.tema.DadosAtualizacaoTema;
import com.example.blogpessoal.domain.dto.tema.DadosCadastroTema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.util.Collections;
import java.util.List;


@Entity
@Table(name= "tb_temas")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE tb_temas SET ativo = false WHERE id=?")
@FilterDef(name = "deletedTemaFilter", parameters = @ParamDef(name="isDeleted", type=Boolean.class))
@Filter(name="deletedTemaFilter", condition="ativo = :isDeleted")
@EqualsAndHashCode(of = "id")
public class Tema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    private String descricao;

    @OneToMany(mappedBy = "tema", cascade= CascadeType.REMOVE)
    @JsonIgnoreProperties("tema")
    private List<Postagem> postagens;

    private Boolean ativo;

    public Tema(DadosAtualizacaoTema tema) {
        this.id = tema.id();
        this.descricao = tema.descricao();
    }

    public Tema(DadosCadastroTema tema) {
        this.titulo = tema.titulo();
        this.descricao = tema.descricao();
        this.postagens = Collections.emptyList();
        this.ativo = true;
    }

    public void atualizarInformacoes(DadosAtualizacaoTema dados) {
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
    }

}

package com.example.blogpessoal.domain.modelos;

import com.example.blogpessoal.domain.dto.postagem.DadosAtualizacaoPostagem;
import com.example.blogpessoal.domain.dto.postagem.DadosCadastroPostagem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name= "tb_postagens")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String texto;

    @UpdateTimestamp
    private LocalDateTime data;

    @ManyToOne
    @JsonIgnoreProperties("postagems")
    private Tema tema;
    private Boolean ativo;
    @ManyToOne
    @JsonIgnoreProperties("postagems")
    private Usuario usuario;

    public Postagem(DadosCadastroPostagem dados) {
        this.titulo = dados.titulo();
        this.texto = dados.texto();
        this.data = dados.data();
        this.tema = new Tema(dados.tema());
        this.ativo = true;
    }

    public void excluir() {
        this.ativo = false;
    }

    public void atualizarInformacoes(DadosAtualizacaoPostagem dados) {
        this.titulo = dados.titulo();
        this.texto = dados.texto();
        this.data = dados.data();
        this.tema = new Tema(dados.tema());
    }
}

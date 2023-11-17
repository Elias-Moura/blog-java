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
    @JsonIgnoreProperties("postagens")
    private Tema tema;
    private Boolean ativo;
    @ManyToOne
    @JsonIgnoreProperties("postagens")
    private Usuario usuario;

    public Postagem(DadosCadastroPostagem dados, Usuario usuario, Tema tema) {
        this.titulo = dados.titulo();
        this.texto = dados.texto();
        this.data = dados.data();
        this.tema = tema;
        this.usuario = usuario;
        this.ativo = true;
    }

    public void excluir() {
        this.ativo = false;
    }

    public void atualizarInformacoes(DadosAtualizacaoPostagem dados) {
        if (dados.titulo() != null){
            this.titulo = dados.titulo();
        }
        if (dados.texto() != null) {
            this.texto = dados.texto();
        }
        if (dados.data() != null){
            this.data = dados.data();
        }
        if (dados.tema() != null){
            this.tema = new Tema(dados.tema());
        }
    }
}

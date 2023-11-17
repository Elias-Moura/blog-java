package com.example.blogpessoal.domain.service;

import com.example.blogpessoal.domain.dto.tema.DadosAtualizacaoTema;
import com.example.blogpessoal.domain.dto.tema.DadosCadastroTema;
import com.example.blogpessoal.domain.dto.tema.DadosListagemTema;
import com.example.blogpessoal.domain.modelos.Tema;
import com.example.blogpessoal.domain.repository.TemaRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemaService {
    @Autowired
    private TemaRepository repository;

    @Autowired
    private EntityManager entityManager;

    public Tema create(DadosCadastroTema dados){
        var tema = new Tema(dados);
        repository.save(tema);
        return tema;
    }

    public void remove(Long id) {
        repository.deleteById(id);
    }

    public Page<DadosListagemTema> findAll(Pageable paginacao, boolean active){
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedTemaFilter");
        filter.setParameter("isDeleted", active);
        Page<DadosListagemTema> temas = repository.findAll(paginacao).map(DadosListagemTema::new);
        session.disableFilter("deletedTemaFilter");
        return temas;
    }

    public DadosListagemTema update(DadosAtualizacaoTema dados){
        var tema = repository.getReferenceById(dados.id());
        tema.atualizarInformacoes(dados);
        return new DadosListagemTema(tema);
    }

    public DadosListagemTema findById(Long id){
        Tema tema = repository.getReferenceById(id);
        return new DadosListagemTema(tema);
    }


    public List<DadosListagemTema> findByTitulo(String titulo) {
        return repository.findAllByTituloContainingIgnoreCase(titulo).stream().map(DadosListagemTema::new).toList();
    }
}

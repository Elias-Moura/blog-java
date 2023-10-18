package com.example.blogpessoal.controller;

import com.example.blogpessoal.domain.dto.tema.DadosAtualizacaoTema;
import com.example.blogpessoal.domain.dto.tema.DadosCadastroTema;
import com.example.blogpessoal.domain.dto.tema.DadosListagemTema;
import com.example.blogpessoal.domain.modelos.Tema;
import com.example.blogpessoal.domain.repository.TemaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

    @Autowired
    private TemaRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemTema>> getAll(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListagemTema::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemTema> getById(@PathVariable Long id) {
        var tema = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemTema(tema));
    }

    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<DadosListagemTema>> getByTitle(@PathVariable String descricao) {
        var body = repository.findAllByDescricaoContainingIgnoreCase(descricao)
                .stream()
                .map(DadosListagemTema::new)
                .toList();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemTema> post(@Valid @RequestBody DadosCadastroTema dados, UriComponentsBuilder uriBuilder) {
        var tema = new Tema(dados);
        repository.save(tema);

        var uri = uriBuilder.path("/tema/{id}").buildAndExpand(tema.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemTema(tema));
    }

    @PutMapping
    @Transactional
    public ResponseEntity put(@RequestBody @Valid DadosAtualizacaoTema dados) {
        var tema = repository.getReferenceById(dados.id());
        tema.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosListagemTema(tema));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        var tema = repository.getReferenceById(id);
        tema.excluir();

        return ResponseEntity.noContent().build();
    }

}

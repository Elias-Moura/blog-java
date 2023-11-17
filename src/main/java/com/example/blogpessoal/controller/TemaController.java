package com.example.blogpessoal.controller;

import com.example.blogpessoal.domain.dto.tema.DadosAtualizacaoTema;
import com.example.blogpessoal.domain.dto.tema.DadosCadastroTema;
import com.example.blogpessoal.domain.dto.tema.DadosListagemTema;
import com.example.blogpessoal.domain.modelos.Tema;
import com.example.blogpessoal.domain.service.TemaService;
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
    private TemaService service;

    @GetMapping
    public ResponseEntity<Page<DadosListagemTema>> getAll(
                @PageableDefault(size = 10, sort = {"id"}) Pageable paginacao, boolean isActive
        ) {
        var page = service.findAll(paginacao, isActive);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemTema> getById(@PathVariable Long id) {
        var tema = service.findById(id);
        return ResponseEntity.ok(tema);
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<DadosListagemTema>> getByTitle(@PathVariable String titulo) {
        var body = service.findByTitulo(titulo);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemTema> post(@Valid @RequestBody DadosCadastroTema dados, UriComponentsBuilder uriBuilder) {
        Tema tema = service.create(dados);
        var uri = uriBuilder.path("/tema/{id}").buildAndExpand(tema.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemTema(tema));
    }

    @PutMapping
    @Transactional
    public ResponseEntity put(@RequestBody @Valid DadosAtualizacaoTema dados) {
        DadosListagemTema dadosListagemTema = service.update(dados);
        return ResponseEntity.ok(dadosListagemTema);
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }

}

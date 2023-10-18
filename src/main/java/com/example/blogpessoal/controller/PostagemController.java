package com.example.blogpessoal.controller;

import com.example.blogpessoal.domain.dto.postagem.DadosAtualizacaoPostagem;
import com.example.blogpessoal.domain.dto.postagem.DadosCadastroPostagem;
import com.example.blogpessoal.domain.dto.postagem.DadosListagemPostagem;
import com.example.blogpessoal.domain.modelos.Postagem;
import com.example.blogpessoal.domain.repository.PostagemRepository;
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
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

    @Autowired
    private PostagemRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemPostagem>> getAll(@PageableDefault(size=10, sort={"data"}) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListagemPostagem::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemPostagem> getById(@PathVariable Long id) {
        var postagem = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemPostagem(postagem));
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<DadosListagemPostagem>> getByTitulo(@PathVariable String titulo) {
        var body = repository.findAllByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(DadosListagemPostagem::new)
                .toList();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosCadastroPostagem> post(@RequestBody @Valid DadosCadastroPostagem dados, UriComponentsBuilder uriBuilder) {
        var postagem = new Postagem(dados);
        repository.save(postagem);

        var uri = uriBuilder.path("/postagem/{id}").buildAndExpand(postagem.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosCadastroPostagem(postagem));
    }

    @DeleteMapping({"/{id}"})
    @Transactional
    public ResponseEntity delete(@PathVariable Long id){
        Postagem postagem = repository.getReferenceById(id);
        postagem.excluir();

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<DadosListagemPostagem> put(@Valid @RequestBody DadosAtualizacaoPostagem dados) {
        Postagem postagem = repository.getReferenceById(dados.id());
        postagem.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemPostagem(postagem));
    }
}

package com.example.blogpessoal.controller;

import com.example.blogpessoal.domain.dto.postagem.DadosAtualizacaoPostagem;
import com.example.blogpessoal.domain.dto.postagem.DadosCadastroPostagem;
import com.example.blogpessoal.domain.dto.postagem.DadosListagemPostagem;
import com.example.blogpessoal.domain.modelos.Postagem;
import com.example.blogpessoal.domain.modelos.Tema;
import com.example.blogpessoal.domain.modelos.Usuario;
import com.example.blogpessoal.domain.repository.PostagemRepository;
import com.example.blogpessoal.domain.repository.TemaRepository;
import com.example.blogpessoal.domain.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

    @Autowired
    private PostagemRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TemaRepository temaRepository;

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
        Optional<Usuario> usuario = usuarioRepository.findById(dados.usuarioId());
        Optional<Tema> tema = temaRepository.findById(dados.temaId());

        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuário não existe.");
        }

        if (tema.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tema informado não existe." );
        }

        var postagem = new Postagem(dados , usuario.get(), tema.get());
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
    public ResponseEntity<DadosAtualizacaoPostagem> put(@Valid @RequestBody DadosAtualizacaoPostagem dados) {
        Postagem postagem = repository.getReferenceById(dados.id());
        postagem.atualizarInformacoes(dados);
        repository.save(postagem);
        return ResponseEntity.ok(new DadosAtualizacaoPostagem(postagem));
    }
}

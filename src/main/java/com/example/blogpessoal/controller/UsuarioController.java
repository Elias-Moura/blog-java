package com.example.blogpessoal.controller;

import com.example.blogpessoal.domain.dto.security.DadosAutenticacao;
import com.example.blogpessoal.domain.dto.security.DadosToken;
import com.example.blogpessoal.domain.dto.usuario.DadosBodyAtaualizacaoUsuario;
import com.example.blogpessoal.domain.dto.usuario.DadosCadastroUsuario;
import com.example.blogpessoal.domain.dto.usuario.DadosListagemUsuario;
import com.example.blogpessoal.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    @PostMapping("/cadastrar")
    public ResponseEntity<DadosListagemUsuario> create(@Valid @RequestBody DadosCadastroUsuario data) {
        DadosListagemUsuario createdUser = usuarioService.create(data);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Transactional
    @PostMapping("/logar")
    public ResponseEntity login(@Valid @RequestBody DadosAutenticacao loginData) {
        DadosToken token = usuarioService.authenticateUser(loginData);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemUsuario>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemUsuario> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Transactional
    @PutMapping
    public ResponseEntity<DadosListagemUsuario> update(@RequestHeader("Authorization") String token, @Valid @RequestBody DadosBodyAtaualizacaoUsuario dados) {
        return ResponseEntity.ok(usuarioService.update(dados, token));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        var httpStatus = usuarioService.destroy(id, token);
        return ResponseEntity.status(httpStatus).build();
    }
}

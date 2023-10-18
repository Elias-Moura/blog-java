package com.example.blogpessoal.services;

import com.example.blogpessoal.domain.dto.security.DadosAutenticacao;
import com.example.blogpessoal.domain.dto.security.DadosToken;
import com.example.blogpessoal.domain.dto.usuario.DadosAtualizacaoUsuario;
import com.example.blogpessoal.domain.dto.usuario.DadosCadastroUsuario;
import com.example.blogpessoal.domain.dto.usuario.DadosListagemUsuario;
import com.example.blogpessoal.domain.modelos.Usuario;
import com.example.blogpessoal.domain.repository.UsuarioRepository;
import com.example.blogpessoal.infra.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    public DadosListagemUsuario create(DadosCadastroUsuario data) {
        if (isEmailAlreadyRegistered(data.email()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email already registered.");

        Usuario usuario = new Usuario(data);
        usuario.setSenha(this.encryptPassword(usuario.getSenha()));

        repository.save(usuario);
        return new DadosListagemUsuario(usuario);
    }

    private boolean isEmailAlreadyRegistered(String email) {
        return repository.existsByEmail(email);
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }


    public DadosListagemUsuario findById(Long id) {
        return repository.findById(id)
                .map((response) -> new DadosListagemUsuario(response))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
    }


    public List<DadosListagemUsuario> findAll() {
        return repository.findAll().stream()
                .map((user) -> new DadosListagemUsuario(user))
                .toList();
    }


    public DadosListagemUsuario update(DadosAtualizacaoUsuario data) {
        String email = jwtService.extractUserEmail(jwtService.extractOnlyHashPartOfToken(data.token()));

        if (!this.isUserExistsByEmail(email) || !this.isUserExistsById(data.id()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");

        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.get().getId() != data.id()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        if (usuario.get().getEmail() != data.email())
            if (this.isEmailInUseByOtherUser(data.email(), data.id()))
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email already registered.");

        usuario.get().setEmail(data.email());
        usuario.get().setImagem(data.imagem());
        usuario.get().setNome(data.nome());
        usuario.get().setSenha(this.encryptPassword(data.senha()));

        repository.save(usuario.get());

        return new DadosListagemUsuario(usuario.get());
    }


    private boolean isUserExistsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    private boolean isUserExistsById(Long id) {
        return repository.existsById(id);
    }

    private boolean isEmailInUseByOtherUser(String email, Long id) {
        return repository.existsByEmailAndIdNot(email, id);
    }


    public void destroy(DadosAtualizacaoUsuario data) {
        String usuarioEmail = jwtService.extractUserEmail(jwtService.extractOnlyHashPartOfToken(data.token()));

        if (!this.isUserExistsByEmail(usuarioEmail) || !this.isUserExistsById(data.id()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");

        Optional<Usuario> usuario = repository.findByEmail(usuarioEmail);

        if (usuario.get().getId() != data.id())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        repository.delete(usuario.get());
    }

    public DadosToken authenticateUser(DadosAutenticacao loginData) {
        var credentials = new UsernamePasswordAuthenticationToken(loginData.email(), loginData.senha());

        Authentication authentication = authenticationManager.authenticate(credentials);

        if (authentication.isAuthenticated()) {
            if (!this.isUserExistsByEmail(loginData.email()))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect email or password.");

            String token = this.generateToken(loginData.email());

            return new DadosToken(token);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect email or password.");
    }

    private String generateToken(String email) {
        return jwtService.generateToken(email);
    }
}
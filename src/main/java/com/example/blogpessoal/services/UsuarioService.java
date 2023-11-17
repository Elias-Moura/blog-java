package com.example.blogpessoal.services;

import com.example.blogpessoal.domain.dto.security.DadosAutenticacao;
import com.example.blogpessoal.domain.dto.security.DadosToken;
import com.example.blogpessoal.domain.dto.usuario.DadosBodyAtaualizacaoUsuario;
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
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email já foi registrado.");

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }


    public List<DadosListagemUsuario> findAll() {
        return repository.findAll().stream()
                .map((user) -> new DadosListagemUsuario(user))
                .toList();
    }


    public DadosListagemUsuario update(DadosBodyAtaualizacaoUsuario dadosAtaualizacao, String token) {
        String email = jwtService.extractUserEmail(jwtService.extractOnlyHashPartOfToken(token));
//        String email = dadosAtaualizacao.email();

        if (!this.isUserExistsByEmail(email) || !this.isUserExistsById(dadosAtaualizacao.id()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email já foi registrado.");

        Usuario usuario = repository.findByEmail(email).get();

        if (usuario.getId() != dadosAtaualizacao.id()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        if (usuario.getEmail() != dadosAtaualizacao.email())
            if (this.isEmailInUseByOtherUser(dadosAtaualizacao.email(), dadosAtaualizacao.id()))
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email já foi registrado.");

        if (dadosAtaualizacao.nome() != null) {
            usuario.setNome(dadosAtaualizacao.nome());
        }
        if (dadosAtaualizacao.email() != null) {
            usuario.setEmail(dadosAtaualizacao.email());
        }
        if (dadosAtaualizacao.imagem() != null) {
            usuario.setImagem(dadosAtaualizacao.imagem());
        }
        if (dadosAtaualizacao.senha() != null){
            usuario.setSenha(this.encryptPassword(dadosAtaualizacao.senha()));
        }

        repository.save(usuario);
        return new DadosListagemUsuario(usuario);
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


    public HttpStatus destroy(Long id, String token) {
        String usuarioEmail = jwtService.extractUserEmail(jwtService.extractOnlyHashPartOfToken(token));
//        String usuarioEmail = data.email();

        if (!this.isUserExistsByEmail(usuarioEmail) || !this.isUserExistsById(id))
            return HttpStatus.UNAUTHORIZED;

        Optional<Usuario> usuario = repository.findByEmail(usuarioEmail);

        if (usuario.get().getId() != id)
            return HttpStatus.FORBIDDEN;

        repository.delete(usuario.get());
        return HttpStatus.NO_CONTENT;
    }

    public DadosToken authenticateUser(DadosAutenticacao loginData) {
        var credentials = new UsernamePasswordAuthenticationToken(loginData.email(), loginData.senha());

        Authentication authentication = authenticationManager.authenticate(credentials);

        if (authentication.isAuthenticated()) {
            if (!this.isUserExistsByEmail(loginData.email()))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email ou senha incorreta.");

            String token = this.generateToken(loginData.email());

            return new DadosToken(token);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email ou senha incorreta.");
    }

    private String generateToken(String email) {
        return jwtService.generateToken(email);
    }
}
package com.example.blogpessoal.infra.security;

import com.example.blogpessoal.domain.dto.security.DadosAutenticacao;
import com.example.blogpessoal.domain.modelos.Usuario;
import com.example.blogpessoal.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = repository.findByEmail(email);

        if (user.isEmpty()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return new UserDetailsImpl(new DadosAutenticacao(user.get()));
    }
}

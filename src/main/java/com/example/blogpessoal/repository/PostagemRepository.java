package com.example.blogpessoal.repository;

import com.example.blogpessoal.models.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostagemRepository extends JpaRepository<Postagem,Long> {
}

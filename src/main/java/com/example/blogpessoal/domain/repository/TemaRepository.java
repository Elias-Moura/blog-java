package com.example.blogpessoal.domain.repository;

import com.example.blogpessoal.domain.modelos.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemaRepository extends JpaRepository<Tema,Long> {
    public List<Tema> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
}

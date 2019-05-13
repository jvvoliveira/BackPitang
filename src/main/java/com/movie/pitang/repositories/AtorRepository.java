package com.movie.pitang.repositories;

import com.movie.pitang.models.Ator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtorRepository extends JpaRepository<Ator, Long> {
    public List<Ator> findByNomeLike(String nome);
    public Page<Ator> findByNomeContainingIgnoreCase(String nome, Pageable pagebale);
    public boolean existsByNome(String nome);
    public Ator findByIdLike(long id);
    public Ator findByIdapiLike(long id);
}

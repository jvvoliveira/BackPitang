package com.movie.pitang.repositories;

import com.movie.pitang.models.Filme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmeRepository extends JpaRepository<Filme, Long> {

    public Page<Filme> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    public Page<Filme> findByLinguaLike(String lingua, Pageable pageable);
    public Page<Filme> findByAnoLancamentoLike(int ano, Pageable pageable);
    public Filme findByIdapiLike(long id);
    public Filme findById(long id);
    public Filme deleteById(long id);
}

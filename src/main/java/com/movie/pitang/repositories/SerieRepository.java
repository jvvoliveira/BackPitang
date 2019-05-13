package com.movie.pitang.repositories;

import com.movie.pitang.models.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    public Page<Serie> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    public Page<Serie> findByLinguaLike(String lingua, Pageable pageable);
    public Page<Serie> findByAnoLancamentoLike(int ano, Pageable pageable);
    public Serie findByIdapiLike(long id);
}

package com.movie.pitang.repositories;

import com.movie.pitang.models.Produtor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutorRepository extends JpaRepository<Produtor, Long> {
    public List<Produtor> findByNomeLike(String nome);
    public boolean existsByNome(String nome);
    public Page<Produtor> findByNomeContainingIgnoreCase(String nome, Pageable pagebale);
    public Page<Produtor> findByNomeContainingIgnoreCaseAndTipoLike(String nome, String tipo, Pageable pageable);
    public Produtor findByIdLike(long id);
    public Produtor findByIdapiLike(long id);
    public Page<Produtor> findByTipoLike(String tipo, Pageable pagebale);
}

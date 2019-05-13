package com.movie.pitang.repositories;

import com.movie.pitang.models.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
    public Genero findByIdLike(long id);
    public Genero findByIdapiLike(long id);
    public boolean existsByIdapi(long id);
    public Genero findByDescricaoLike(String descricao);
    public boolean existsByDescricaoLike(String descricao);
}

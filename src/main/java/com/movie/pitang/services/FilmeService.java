package com.movie.pitang.services;

import com.movie.pitang.models.*;
import com.movie.pitang.models.dto.FilmeDTO;
import com.movie.pitang.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmeService{
    @Autowired
    private AtorRepository atorRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private FilmeRepository filmeRepository;

    @Autowired
    private ProdutorRepository produtorRepository;

    public Filme atualizar(long id, FilmeDTO filmedto){

        Filme atualFilme = this.filmeRepository.findById(id);

        if(filmedto.getTitulo() != null){
            atualFilme.setTitulo(filmedto.getTitulo());
        }
        if(filmedto.getLingua() != null){
            atualFilme.setLingua(filmedto.getLingua());
        }
        if(filmedto.getAnoLancamento() != null){
            atualFilme.setAnoLancamento(filmedto.getAnoLancamento());
        }
        if(filmedto.getPaisDeOrigem() != null){
            atualFilme.setPaisDeOrigem(filmedto.getPaisDeOrigem());
        }
        if(filmedto.getDuracao() != null){
            atualFilme.setDuracao(filmedto.getDuracao());
        }
        if(filmedto.getDescricao() != null){
            atualFilme.setDescricao(filmedto.getDescricao());
        }
        if(filmedto.getAtores() != null){ //transformação do nome do ator em objeto Ator
            List<Ator> atores = new ArrayList<>();
            for(String nomeAtor : filmedto.getAtores()){
                if(atorRepository.existsByNome(nomeAtor)) {
                    atores.addAll(atorRepository.findByNomeLike(nomeAtor));
                }
            }
            atualFilme.setAtores(atores);
        }
        if(filmedto.getProdutores() != null){ //transformação do nome do produtor em objeto Produtor
            List<Produtor> produtores = new ArrayList<>();
            for(String nomeProdutor : filmedto.getProdutores()){
                if(produtorRepository.existsByNome(nomeProdutor)) {
                    produtores.addAll(produtorRepository.findByNomeLike(nomeProdutor));
                }
            }
            atualFilme.setProdutores(produtores);
        }
        if(filmedto.getGeneros() != null){ //transformação do nome do gênero em objeto Genero
            List<Genero> generos = new ArrayList<>();
            for(String nomeGenero : filmedto.getGeneros()){
                if(generoRepository.existsByDescricaoLike(nomeGenero)) {
                    generos.add(generoRepository.findByDescricaoLike(nomeGenero));
                }
            }
            atualFilme.setGeneros(generos);
        }

        return atualFilme;
    }

}

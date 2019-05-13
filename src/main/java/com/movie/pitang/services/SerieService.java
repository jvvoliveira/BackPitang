package com.movie.pitang.services;

import com.movie.pitang.models.*;
import com.movie.pitang.models.dto.SerieDTO;
import com.movie.pitang.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SerieService {
    @Autowired
    private AtorRepository atorRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private ProdutorRepository produtorRepository;

    public Serie atualizar(long id, SerieDTO serieDTO){

        Serie atualSerie = this.serieRepository.findById(id).get();

        if(serieDTO.getTitulo() != null){
            atualSerie.setTitulo(serieDTO.getTitulo());
        }
        if(serieDTO.getLingua() != null){
            atualSerie.setLingua(serieDTO.getLingua());
        }
        if(serieDTO.getAnoLancamento() != null){
            atualSerie.setAnoLancamento(serieDTO.getAnoLancamento());
        }
        if(serieDTO.getDuracao() != null){
            atualSerie.setDuracao(serieDTO.getDuracao());
        }
        if(serieDTO.getTemporadas() != null){
            atualSerie.setTemporadas(serieDTO.getTemporadas());
        }
        if(serieDTO.getPaisDeOrigem() != null){
            atualSerie.setPaisDeOrigem(serieDTO.getPaisDeOrigem());
        }
        if(serieDTO.getDescricao() != null){
            atualSerie.setDescricao(serieDTO.getDescricao());
        }
        if(serieDTO.getAtores() != null){
            List<Ator> atores = new ArrayList<>();
            for(String nomeAtor : serieDTO.getAtores()){
                if(atorRepository.existsByNome(nomeAtor)) {
                    atores.addAll(atorRepository.findByNomeLike(nomeAtor));
                }
            }
            atualSerie.setAtores(atores);
        }
        if(serieDTO.getProdutores() != null){
            List<Produtor> produtores = new ArrayList<>();
            for(String nomeProdutor : serieDTO.getProdutores()){
                if(produtorRepository.existsByNome(nomeProdutor)) {
                    produtores.addAll(produtorRepository.findByNomeLike(nomeProdutor));
                }
            }
            atualSerie.setProdutores(produtores);
        }
        if(serieDTO.getGeneros() != null){
            List<Genero> generos = new ArrayList<>();
            for(String nomeGenero : serieDTO.getGeneros()){
                if(generoRepository.existsByDescricaoLike(nomeGenero)) {
                    generos.add(generoRepository.findByDescricaoLike(nomeGenero));
                }
            }
            atualSerie.setGeneros(generos);
        }

        return atualSerie;
    }
}

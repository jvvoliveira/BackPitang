package com.movie.pitang.services;

import com.movie.pitang.models.Ator;
import com.movie.pitang.models.Filme;
import com.movie.pitang.models.Produtor;
import com.movie.pitang.models.Serie;
import com.movie.pitang.models.dto.PessoaDTO;
import com.movie.pitang.repositories.AtorRepository;
import com.movie.pitang.repositories.FilmeRepository;
import com.movie.pitang.repositories.ProdutorRepository;
import com.movie.pitang.repositories.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private AtorRepository atorRepository;

    @Autowired
    private ProdutorRepository produtorRepository;

    @Autowired
    private FilmeRepository filmeRepository;

    @Autowired
    private SerieRepository serieRepository;

    public Ator atualizarAtor(long id, PessoaDTO pessoadto){

        Ator atualAtor = this.atorRepository.findById(id);

        if(pessoadto.getAltura() != null){
            atualAtor.setAltura(pessoadto.getAltura());
        }
        if(pessoadto.getCidadeNatal() != null){
            atualAtor.setCidadeNatal(pessoadto.getCidadeNatal());
        }
        if(pessoadto.getGenero() != null){
            atualAtor.setGenero(pessoadto.getGenero());
        }
        if(pessoadto.getNome() != null){
            atualAtor.setNome(pessoadto.getNome());
        }
        if(pessoadto.getPaisOndeMora() != null){
            atualAtor.setPaisOndeMora(pessoadto.getPaisOndeMora());
        }
        return atualAtor;
    }

    public void  deleteAtor(long id){
        boolean achou = false;
        int index = 0;
        List<Filme> filmes = this.filmeRepository.findAll();
        for(Filme filme : filmes){
            List<Ator> atores = filme.getAtores();
            for(int i = 0; i < atores.size(); i++){
                if(atores.get(i).getId() == id){
                    index = i;
                    achou = true;
                }
            }
            if(achou){
                achou = false;
                atores.remove(index);
            }
        }
        List<Serie> series = this.serieRepository.findAll();
        for(Serie serie : series){
            List<Ator> atores = serie.getAtores();
            for(int i = 0; i < atores.size(); i++){
                if(atores.get(i).getId() == id){
                    index = i;
                    achou = true;
                }
            }
            if(achou){
                achou = false;
                atores.remove(index);
            }
        }
    }
    public Produtor atualizarProdutor(long id, PessoaDTO pessoadto){

        Produtor atualProdutor = this.produtorRepository.findByIdLike(id);

        if(pessoadto.getNome() != null){
            atualProdutor.setNome(pessoadto.getNome());
        }
        if(pessoadto.getAltura() != null){
            atualProdutor.setAltura(pessoadto.getAltura());
        }
        if(pessoadto.getCidadeNatal() != null){
            atualProdutor.setCidadeNatal(pessoadto.getCidadeNatal());
        }
        if(pessoadto.getGenero() != null){
            atualProdutor.setGenero(pessoadto.getGenero());
        }
        if(pessoadto.getNome() != null){
            atualProdutor.setNome(pessoadto.getNome());
        }
        if(pessoadto.getPaisOndeMora() != null){
            atualProdutor.setPaisOndeMora(pessoadto.getPaisOndeMora());
        }
        return atualProdutor;
    }
    public void  deleteProdutor(long id){
        boolean achou = false;
        int index = 0;
        List<Filme> filmes = this.filmeRepository.findAll();
        for(Filme filme : filmes){
            List<Produtor> produtores = filme.getProdutores();
            for(int i = 0; i < produtores.size(); i++){
                if(produtores.get(i).getId() == id){
                    index = i;
                    achou = true;
                }
            }
            if(achou){
                achou = false;
                produtores.remove(index);
            }
        }
        List<Serie> series = this.serieRepository.findAll();
        for(Serie serie : series){
            List<Produtor> produtores = serie.getProdutores();
            for(int i = 0; i < produtores.size(); i++){
                if(produtores.get(i).getId() == id){
                    index = i;
                    achou = true;
                }
            }
            if(achou){
                achou = false;
                produtores.remove(index);
            }
        }
    }
}

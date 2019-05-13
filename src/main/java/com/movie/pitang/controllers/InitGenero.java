package com.movie.pitang.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.pitang.models.Genero;
import com.movie.pitang.models.api.GeneroAPI;
import com.movie.pitang.models.results.GeneroResult;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitGenero {

    private static final String APIKEY = "e3fa50c6b092087dc2cc2d6a7e4f5367";

    private String urlFilme = "https://api.themoviedb.org/3/genre/movie/list" +
            "?api_key="+APIKEY+"&language=pt-br";

    private String urlSerie = "https://api.themoviedb.org/3/genre/tv/list" +
            "?api_key="+APIKEY+"&language=pt-br";

    public InitGenero(){

    }

    public List<Genero> listarGeneros(){

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String retornoFilme = restTemplate.getForObject(urlFilme, String.class);
        String retornoSerie = restTemplate.getForObject(urlSerie, String.class);

        GeneroAPI generoAPIFilme = new GeneroAPI();
        GeneroAPI generoAPISerie = new GeneroAPI();
        try {
            generoAPIFilme = mapper.readValue(retornoFilme, GeneroAPI.class);
            generoAPISerie = mapper.readValue(retornoSerie, GeneroAPI.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Genero> generos = new ArrayList<>();
        for(GeneroResult genApi : generoAPIFilme.getGenres()){
            Genero genero = new Genero();
            genero.setIdapi(genApi.getId());
            genero.setDescricao(genApi.getName());
            generos.add(genero);
        }
        for(GeneroResult genApi : generoAPISerie.getGenres()){
            if(!existe(generos, genApi.getId())) {
                Genero genero = new Genero();
                genero.setIdapi(genApi.getId());
                genero.setDescricao(genApi.getName());
                generos.add(genero);
            }
        }
        return generos;
    }
    public boolean existe(List<Genero> lista, int i){ //validação com o id da API
        for(Genero gen : lista){
            if(gen.getIdapi() == i){
                return true;
            }
        }
        return false;
    }


}

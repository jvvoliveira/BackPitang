package com.movie.pitang.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.pitang.models.*;
import com.movie.pitang.models.api.SerieAPI;
import com.movie.pitang.models.results.SerieResult;
import com.movie.pitang.repositories.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitSerie {

    private static final String APIKEY = "e3fa50c6b092087dc2cc2d6a7e4f5367";

    private static String url = "https://api.themoviedb.org/3/discover/tv" +
            "?api_key="+APIKEY+"&language=pt-br";

    private String idSerie;

    @Autowired
    private GeneroRepository generoRepository;

    private List<Ator> todosAtores;
    private List<Produtor> todosProdutores;

    public InitSerie(){

    }

    public List<Serie> listarSeries(){
        todosAtores = new ArrayList<>();
        todosProdutores = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String retorno = restTemplate.getForObject(url, String.class);
        SerieAPI serieAPI = new SerieAPI();
        try {
            serieAPI = mapper.readValue(retorno, SerieAPI.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Serie> series = new ArrayList<>();
        for(SerieResult serieResult : serieAPI.getResults()){
            this.idSerie = String.valueOf(serieResult.getId());
            Serie serie = new Serie();
            serie.setIdapi(serieResult.getId());
            serie.setTitulo(serieResult.getOriginal_name());
            serie.setAnoLancamento(Integer.parseInt(serieResult.getFirst_air_date().substring(0,4)));
            serie.setLingua(serieResult.getOriginal_language());
            serie.setDescricao(serieResult.getOverview());
            serie.setPaisDeOrigem(serieResult.getOrigin_country().toString());

            addMaisDetalhes(serie);
            obterPessoas(serie);

            series.add(serie);
        }
        return series;
    }
    public void addMaisDetalhes(Serie serie){
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String urlUnica = "https://api.themoviedb.org/3/tv/"+this.idSerie +
                "?api_key="+APIKEY+"&language=pt-br%22";

        String retorno = restTemplate.getForObject(urlUnica, String.class);
        try {
            JsonNode jsonNode = mapper.readTree(retorno);
            serie.setTemporadas(jsonNode.get("number_of_seasons").asInt());
            serie.setDuracao(jsonNode.get("episode_run_time").get(0).asLong());
            if(jsonNode.get("poster_path") != null) {
                serie.setPoster(jsonNode.get("poster_path").textValue());
            }
            if(jsonNode.get("backdrop_path") != null) {
                serie.setImagem(jsonNode.get("backdrop_path").textValue());
            }
            List<Genero> generos = new ArrayList<>();
            int tam = jsonNode.get("genres").size();
            for(int i = 0; i < tam; i++){
                long id = jsonNode.get("genres").get(i).get("id").asLong();
                if(this.generoRepository.existsByIdapi(id)) {
                    Genero genero = this.generoRepository.findByIdapiLike(id);
                    generos.add(genero);
                }
            }
            serie.setGeneros(generos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void obterPessoas(Serie serie){
        String urlCredits = "https://api.themoviedb.org/3/tv/"+this.idSerie +"/credits" +
                "?api_key="+APIKEY+"&language=pt-br";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String retorno = restTemplate.getForObject(urlCredits, String.class);
        try {
            JsonNode jsonNode = mapper.readTree(retorno);
            List<Ator> atores = new ArrayList<>();
            for(int i = 0; i < 5; i++ ){
                if(jsonNode.get("cast").get(i) != null) {
                    long atorID = jsonNode.get("cast").get(i).get("id").asLong();
                    String urlPessoa = "https://api.themoviedb.org/3/person/" + atorID +
                            "?api_key="+APIKEY+"&language=pt-br";
                    Ator ator = new Ator();
                    ator.setIdapi(atorID);
                    if (!this.todosAtores.contains(ator)) {
                        String retornoAtor = restTemplate.getForObject(urlPessoa, String.class);
                        JsonNode jsonNodeAtor = mapper.readTree(retornoAtor);
                        ator.setIdapi(jsonNodeAtor.get("id").asLong());
                        ator.setGenero(String.valueOf(jsonNodeAtor.get("gender").asInt()));
                        ator.setNome(jsonNodeAtor.get("name").textValue());
                        ator.setCidadeNatal(jsonNodeAtor.get("place_of_birth").textValue());
                        if(jsonNodeAtor.get("profile_path") != null) {
                            ator.setPoster(jsonNodeAtor.get("profile_path").textValue());
                        }
                        this.todosAtores.add(ator);
                    } else {
                        int posicao = this.todosAtores.indexOf(ator);
                        ator = this.todosAtores.get(posicao);
                    }

                    atores.add(ator);
                    Thread.sleep(200);
                }
            }
            serie.setAtores(atores);

            List<Produtor> produtores = new ArrayList<>();
            int inseriu = 0;
            int i = 0;
            boolean eDiretor, eAutor, funcaodiferente = false;
            while(inseriu < 5 && i < 15){
                if(jsonNode.get("crew").get(i) != null) {
                    eDiretor = jsonNode.get("crew").get(i).get("department").textValue().equals("Directing");
                    eAutor = jsonNode.get("crew").get(i).get("department").textValue().equals("Writing");
                }else{
                    eDiretor = false;
                    eAutor = false;
                }
                if( eDiretor || eAutor) {
                    int pessoaId = jsonNode.get("crew").get(i).get("id").asInt();
                    String urlPessoa = "https://api.themoviedb.org/3/person/" + pessoaId +
                            "?api_key="+APIKEY+"&language=pt-br";
                    String retornoPessoa = restTemplate.getForObject(urlPessoa, String.class);
                    JsonNode jsonNodePessoa = mapper.readTree(retornoPessoa);

                    Produtor produtor = new Produtor();
                    produtor.setIdapi(pessoaId);
                    if(this.todosProdutores.contains(produtor)){ //mesma pessoa com função diferente
                        int posicao = this.todosProdutores.indexOf(produtor);
                        Produtor prod = this.todosProdutores.get(posicao);
                        if(!prod.getTipo().equals(jsonNode.get("crew").get(i).get("department").textValue())){
                            funcaodiferente = true;
                        }
                    }

                    if(!this.todosProdutores.contains(produtor) || funcaodiferente) {
                        funcaodiferente = false;
                        produtor = new Produtor();
                        produtor.setIdapi(jsonNodePessoa.get("id").asLong());
                        produtor.setGenero(String.valueOf(jsonNodePessoa.get("gender").asInt()));
                        produtor.setNome(jsonNodePessoa.get("name").textValue());
                        produtor.setCidadeNatal(jsonNodePessoa.get("place_of_birth").textValue());
                        if(jsonNodePessoa.get("profile_path") != null) {
                            produtor.setPoster(jsonNodePessoa.get("profile_path").textValue());
                        }
                        if(eDiretor){
                            produtor.setTipo("Directing");
                        }else if(eAutor){
                            produtor.setTipo("Writing");
                        }
                        this.todosProdutores.add(produtor);
                    }else{
                        int posicao = this.todosProdutores.indexOf(produtor);
                        produtor = this.todosProdutores.get(posicao);
                    }
                    produtores.add(produtor);
                    inseriu++;
                }
                i++;
                Thread.sleep(200);
            }
            serie.setProdutores(produtores);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Erro no Thread sleep");
            e.printStackTrace();
        }

    }
}

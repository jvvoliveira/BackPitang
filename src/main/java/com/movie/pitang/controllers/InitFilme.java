package com.movie.pitang.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.pitang.models.*;
import com.movie.pitang.models.api.FilmeAPI;
import com.movie.pitang.models.results.FilmeResult;
import com.movie.pitang.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitFilme {
    private static final String APIKEY = "e3fa50c6b092087dc2cc2d6a7e4f5367";

    private static String url = "https://api.themoviedb.org/3/discover/movie" +
            "?api_key="+APIKEY+"&language=pt-br";

    private String idFilme;

    @Autowired
    private GeneroRepository generoRepository;

    private List<Ator> todosAtores; //garantir que atores não se repitam
    private List<Produtor> todosProdutores; //garantir que produtores não se repitam

    public InitFilme(){

    }

    public List<Filme> listarFilmes(){
        todosAtores = new ArrayList<>();
        todosProdutores = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String retorno = restTemplate.getForObject(url, String.class);
        FilmeAPI filmeAPI = new FilmeAPI();

        try {
            filmeAPI = mapper.readValue(retorno, FilmeAPI.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Filme> filmes = new ArrayList<>();
        for(FilmeResult filmeResult : filmeAPI.getResults()){
            this.idFilme = String.valueOf(filmeResult.getId());
            Filme filme = new Filme();
            filme.setIdapi(filmeResult.getId());
            filme.setTitulo(filmeResult.getOriginal_title());
            filme.setAnoLancamento(Integer.parseInt(filmeResult.getRelease_date().substring(0,4)));
            filme.setLingua(filmeResult.getOriginal_language());
            filme.setDescricao(filmeResult.getOverview());

            List<Genero> generos = new ArrayList<>();
            for(long i : filmeResult.getGenre_ids()){
                Genero genero = this.generoRepository.findByIdapiLike(i);
                generos.add(genero);
            }
            filme.setGeneros(generos);
            maisDetalhes(filme);
            obterPessoas(filme);
            filmes.add(filme);
        }
        return filmes;
    }

    private void maisDetalhes(Filme filme){
        String urlUnica = "https://api.themoviedb.org/3/movie/"+this.idFilme+
                "?api_key="+APIKEY+"&language=pt-br";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String retorno = restTemplate.getForObject(urlUnica, String.class);
        try {
            JsonNode jsonNode = mapper.readTree(retorno);
            filme.setDuracao(jsonNode.get("runtime").asLong());
            if(jsonNode.get("poster_path") != null) {
                filme.setPoster(jsonNode.get("poster_path").textValue());
            }
            if(jsonNode.get("backdrop_path") != null) {
                filme.setImagem(jsonNode.get("backdrop_path").textValue());
            }
            if(jsonNode.get("production_countries").get(0) != null) {
                filme.setPaisDeOrigem(jsonNode.get("production_countries").get(0).get("name").textValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void obterPessoas(Filme filme){
        String urlCredits = "https://api.themoviedb.org/3/movie/"+this.idFilme+"/credits" +
                "?api_key="+APIKEY+"&language=pt-br";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String retorno = restTemplate.getForObject(urlCredits, String.class);
        try {
            JsonNode jsonNode = mapper.readTree(retorno);
            List<Ator> atores = new ArrayList<>();
            for(int i = 0; i < 5; i++ ){
                long atorID = jsonNode.get("cast").get(i).get("id").asLong();
                String urlPessoa = "https://api.themoviedb.org/3/person/"+atorID+
                        "?api_key="+APIKEY+"&language=pt-br";
                Ator ator = new Ator();
                ator.setIdapi(atorID);
                if(!this.todosAtores.contains(ator)) {
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
                }else{
                    int posicao = this.todosAtores.indexOf(ator);
                    ator = this.todosAtores.get(posicao);
                }

                atores.add(ator);
                Thread.sleep(200);
            }
            filme.setAtores(atores);

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
            filme.setProdutores(produtores);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Erro no Thread sleep");
            e.printStackTrace();
        }

    }
}

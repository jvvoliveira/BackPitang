package com.movie.pitang;

import com.movie.pitang.controllers.FilmeController;
import com.movie.pitang.error.ResourceNotFoundException;
import com.movie.pitang.models.Filme;
import com.movie.pitang.repositories.FilmeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TesteFilmeController {

    @Mock
    private FilmeController filmeController;

    @Mock
    private FilmeRepository filmeRepository;

    private Pageable pageable;

    private List<Filme> listaFilmes;

    private List<Filme> criarDados(){
        List<Filme> filmes = new ArrayList<>();
        Filme filme1 = new Filme();
        filme1.setId(1111);
        filme1.setTitulo("Vingadores");
        filme1.setAnoLancamento(2019);
        filme1.setLingua("en");
        Filme filme2 = new Filme();
        filme2.setId(2222);
        filme2.setTitulo("Thor");
        filme2.setAnoLancamento(2018);
        filme2.setLingua("en");
        Filme filme3 = new Filme();
        filme3.setId(3333);
        filme3.setTitulo("Homem de Ferro");
        filme3.setAnoLancamento(2017);
        filme3.setLingua("en");
        filmes.add(filme1);
        filmes.add(filme2);
        filmes.add(filme3);

        return filmes;
    }

    private Filme pegarPorId(long id){
        for(Filme filme : this.listaFilmes){
            if(filme.getId() == id){
                return filme;
            }
        }
        return null;
    }

    private List<Filme> pegarPorAno(int ano){
        List<Filme> lista = new ArrayList<>();
        for(Filme filme : lista){
            if(filme.getAnoLancamento() == ano){
                lista.add(filme);
            }
        }
        return lista;
    }
    private List<Filme> pegarPorLingua(String lingua){
        List<Filme> lista = new ArrayList<>();
        for(Filme filme : lista){
            if(filme.getLingua().equals(lingua)){
                lista.add(filme);
            }
        }
        return lista;
    }
    private List<Filme> pegarPorTitulo(String titulo){
        List<Filme> lista = new ArrayList<>();
        for(Filme filme : lista){
            if(filme.getTitulo().equals(titulo)){
                lista.add(filme);
            }
        }
        return lista;
    }

    private boolean existePorId(long id) {
        Filme filme = new Filme();
        filme.setId(id);
        if(this.listaFilmes.contains(filme)){
            return true;
        }
        return false;
    }
    private Filme deletarPorId(long id){
        Filme filme = new Filme();
        filme.setId(id);
        int index = this.listaFilmes.indexOf(filme);
        return this.listaFilmes.remove(index);
    }


    @Before
    public void inicializarDados(){
        pageable = new PageRequest(0,20);

        filmeRepository = Mockito.spy(FilmeRepository.class);
        listaFilmes = criarDados();

        Mockito.doReturn(new PageImpl<>(listaFilmes)).when(filmeRepository).findAll(pageable);
        Mockito.doReturn(pegarPorId(1111)).when(filmeRepository).findById((long) 1111);
        Mockito.doReturn(existePorId(1111)).when(filmeRepository).existsById((long) 1111);
        Mockito.doReturn(new PageImpl<>(pegarPorAno(2019))).when(filmeRepository).findByAnoLancamentoLike(2019, pageable);
        Mockito.doReturn(new PageImpl<>(pegarPorTitulo("vingadores"))).when(filmeRepository).findByTituloContainingIgnoreCase("vingadores", pageable);
        Mockito.doReturn(new PageImpl<>(pegarPorTitulo("titulo inexistente"))).when(filmeRepository).findByTituloContainingIgnoreCase("titulo inexistente", pageable);
        Mockito.doReturn(new PageImpl<>(pegarPorLingua("en"))).when(filmeRepository).findByLinguaLike("en", pageable);
        Mockito.doReturn(deletarPorId(2222)).when(filmeRepository).deleteById((long)2222);
        Mockito.doReturn(pegarPorId(2222)).when(filmeRepository).findById((long) 2222);
        Mockito.doReturn(existePorId(2222)).when(filmeRepository).existsById((long) 2222);
//        Mockito.doReturn(deletarPorId(35689)).when(filmeRepository).deleteById((long) 35689);

        this.filmeController = new FilmeController();
        Whitebox.setInternalState(this.filmeController, "filmeRepository", filmeRepository);
    }



    @Test
    public void pegarTodosFilmes(){
        ResponseEntity<?> filme = this.filmeController.getAll(pageable);
        Assert.assertEquals(HttpStatus.OK, filme.getStatusCode());
        Assert.assertTrue(filme.hasBody());
    }

    @Test
    public void pegarFilmeExistentePorId() {
        ResponseEntity<?> filme1 = this.filmeController.getFilmeById((long) 1111);
        Assert.assertEquals(HttpStatus.OK, filme1.getStatusCode());
        Assert.assertTrue(filme1.hasBody());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void pegarFilmeInexistentePorId(){
        ResponseEntity<?> filme = this.filmeController.getFilmeById(35689);
        Assert.assertEquals(HttpStatus.NOT_FOUND, filme.getStatusCode());
        Assert.assertFalse(filme.hasBody());
    }

    @Test
    public void pegarFilmePorAno(){
        ResponseEntity<?> filme = this.filmeController.getFilmeByFields(pageable, null,2019, null);
        Assert.assertEquals(HttpStatus.OK, filme.getStatusCode());
        Assert.assertTrue(filme.hasBody());
    }
    @Test
    public void pegarFilmePorTitulo(){
        ResponseEntity<?> filme = this.filmeController.getFilmeByFields(pageable, "vingadores",null,null);
        Assert.assertEquals(HttpStatus.OK, filme.getStatusCode());
        Assert.assertTrue(filme.hasBody());
    }
    @Test
    public void pegarFilmePorTituloInexistente(){
        ResponseEntity<?> filme = this.filmeController.getFilmeByFields(pageable, "titulo inexistente",null,null);
        Assert.assertEquals(HttpStatus.OK, filme.getStatusCode());
//        Assert.assertFalse(filme.hasBody());
    }
    @Test
    public void pegarFilmePorLingua(){
        ResponseEntity<?> filme = this.filmeController.getFilmeByFields(pageable, null,null,"en");
        Assert.assertEquals(HttpStatus.OK, filme.getStatusCode());
        Assert.assertTrue(filme.hasBody());
    }
    @Test
    public void pegarFilmeSemParametro(){
        ResponseEntity<?> filme = this.filmeController.getFilmeByFields(pageable, null,null,null);
        Assert.assertEquals(HttpStatus.OK, filme.getStatusCode());
//        for(int i = 0; i < this.listaFilmes.size(); i++){
//            if(this.listaFilmes.get(i).getTitulo() != filme.getBody().){
//
//            }
//        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletarFilmePorId(){
        ResponseEntity<?> filme = this.filmeController.deleteFilmeById(2222);
        ResponseEntity<?> filmeBusca = this.filmeController.getFilmeById(2222);
        Assert.assertEquals(HttpStatus.OK, filme.getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND, filmeBusca.getStatusCode());
    }
    @Test(expected = ResourceNotFoundException.class)
    public void deletarFilmePorIdInexistente(){
        ResponseEntity<?> filme = this.filmeController.deleteFilmeById(35689);
        Assert.assertEquals(HttpStatus.NOT_FOUND, filme.getStatusCode());
        Assert.assertFalse(filme.hasBody());
    }

}

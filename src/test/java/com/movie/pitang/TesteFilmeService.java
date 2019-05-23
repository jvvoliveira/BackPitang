package com.movie.pitang;

import com.movie.pitang.models.Ator;
import com.movie.pitang.models.Filme;
import com.movie.pitang.models.Genero;
import com.movie.pitang.models.Produtor;
import com.movie.pitang.models.dto.FilmeDTO;
import com.movie.pitang.repositories.AtorRepository;
import com.movie.pitang.repositories.FilmeRepository;
import com.movie.pitang.repositories.GeneroRepository;
import com.movie.pitang.repositories.ProdutorRepository;
import com.movie.pitang.services.FilmeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TesteFilmeService {

    @Mock
    private FilmeService filmeService;

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private GeneroRepository generoRepository;

    @Mock
    private AtorRepository atorRepository;

    @Mock
    private ProdutorRepository produtorRepository;

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
    private Genero retornaGenero(){
        Genero genero = new Genero();
        genero.setDescricao("terror");
        return genero;
    }
    private List<Ator> retornaAtor(){
        Ator ator = new Ator();
        ator.setNome("Bruce Wayne");
        return Arrays.asList(ator);
    }
    private List<Produtor> retornaProdutor(){
        Produtor produtor = new Produtor();
        produtor.setNome("Stan Lee");
        return Arrays.asList(produtor);
    }

    @Before
    public void inicializarDados(){

        filmeRepository = Mockito.spy(FilmeRepository.class);
        generoRepository = Mockito.spy(GeneroRepository.class);
        atorRepository = Mockito.spy(AtorRepository.class);
        produtorRepository = Mockito.spy(ProdutorRepository.class);

        listaFilmes = criarDados();

        Mockito.doReturn(pegarPorId(2222)).when(filmeRepository).findById((long)2222);
        Mockito.doReturn(true).when(generoRepository).existsByDescricaoLike("terror");
        Mockito.doReturn(retornaGenero()).when(generoRepository).findByDescricaoLike("terror");

        Mockito.doReturn(true).when(atorRepository).existsByNome("Bruce Wayne");
        Mockito.doReturn(retornaAtor()).when(atorRepository).findByNomeLike("Bruce Wayne");

        Mockito.doReturn(true).when(produtorRepository).existsByNome("Stan Lee");
        Mockito.doReturn(retornaProdutor()).when(produtorRepository).findByNomeLike("Stan Lee");

        this.filmeService = new FilmeService();
        Whitebox.setInternalState(this.filmeService, "filmeRepository", filmeRepository);
        Whitebox.setInternalState(this.filmeService, "generoRepository", generoRepository);
        Whitebox.setInternalState(this.filmeService, "atorRepository", atorRepository);
        Whitebox.setInternalState(this.filmeService, "produtorRepository", produtorRepository);
    }

    @Test
    public void atualizarFilme(){
        FilmeDTO filmeDTO = new FilmeDTO();
        filmeDTO.setTitulo("Batman");
        filmeDTO.setAnoLancamento(2003);
        filmeDTO.setDescricao("bla bla");
        filmeDTO.setDuracao((long)100);
        filmeDTO.setLingua("japones");
        filmeDTO.setPaisDeOrigem("Brasil");
        this.filmeService.atualizar(2222, filmeDTO);

        Filme filmeTeste = this.listaFilmes.get(1);
        Assert.assertEquals(2222, filmeTeste.getId().intValue());
        Assert.assertEquals("Batman", filmeTeste.getTitulo());
        Assert.assertEquals(2003, filmeTeste.getAnoLancamento().intValue());
        Assert.assertEquals(100, filmeTeste.getDuracao().intValue());
        Assert.assertEquals("bla bla", filmeTeste.getDescricao());
        Assert.assertEquals("japones", filmeTeste.getLingua());
        Assert.assertEquals("Brasil", filmeTeste.getPaisDeOrigem());
    }
    @Test(expected = NullPointerException.class)
    public void atualizarFilmeIdInexistente(){
        FilmeDTO filmeDTO = new FilmeDTO();
        filmeDTO.setTitulo("Batman");
        filmeDTO.setAnoLancamento(2003);
        this.filmeService.atualizar(5689, filmeDTO);
    }

    @Test
    public void atualizarFilmeComObjetoGenero(){
        Set<String> generos = new HashSet<>();
        generos.add("terror");
        FilmeDTO filmeDTO = new FilmeDTO();
        filmeDTO.setGeneros(generos);

        this.filmeService.atualizar(2222, filmeDTO);

        Filme filmeTeste = this.listaFilmes.get(1);
        Assert.assertEquals(2222, filmeTeste.getId().intValue());
        Assert.assertEquals("terror", filmeTeste.getGeneros().get(0).getDescricao());
    }
    @Test
    public void atualizarFilmeComObjetoAtor(){
        Set<String> atores = new HashSet<>();
        atores.add("Bruce Wayne");
        FilmeDTO filmeDTO = new FilmeDTO();
        filmeDTO.setAtores(atores);

        this.filmeService.atualizar(2222, filmeDTO);

        Filme filmeTeste = this.listaFilmes.get(1);
        Assert.assertEquals(2222, filmeTeste.getId().intValue());
        Assert.assertEquals("Bruce Wayne", filmeTeste.getAtores().get(0).getNome());
    }
    @Test
    public void atualizarFilmeComObjetoProdutor(){
        Set<String> produtores = new HashSet<>();
        produtores.add("Stan Lee");
        FilmeDTO filmeDTO = new FilmeDTO();
        filmeDTO.setProdutores(produtores);

        this.filmeService.atualizar(2222, filmeDTO);

        Filme filmeTeste = this.listaFilmes.get(1);
        Assert.assertEquals(2222, filmeTeste.getId().intValue());
        Assert.assertEquals("Stan Lee", filmeTeste.getProdutores().get(0).getNome());
    }
}

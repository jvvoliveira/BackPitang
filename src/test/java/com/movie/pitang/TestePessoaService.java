package com.movie.pitang;

import com.movie.pitang.models.Ator;
import com.movie.pitang.models.Filme;
import com.movie.pitang.models.Pessoa;
import com.movie.pitang.models.dto.PessoaDTO;
import com.movie.pitang.repositories.AtorRepository;
import com.movie.pitang.repositories.FilmeRepository;
import com.movie.pitang.repositories.ProdutorRepository;
import com.movie.pitang.services.PessoaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestePessoaService {

    @Mock
    private PessoaService pessoaService;
    @Mock
    private AtorRepository atorRepository;

    @Mock
    private ProdutorRepository produtorRepository;

    @Autowired
    private FilmeRepository filmeRepository;

    private List<Ator> listaAtores;
    private List<Filme> listaFilmes;

    private List<Ator> criarDadosAtores(){
        List<Ator> atores = new ArrayList<>();
        Ator ator1 = new Ator();
        ator1.setId(10);
        ator1.setNome("ator1");
        Ator ator2 = new Ator();
        ator2.setId(20);
        ator2.setNome("ator2");
        atores.add(ator1);
        atores.add(ator2);
        return atores;
    }
    private List<Filme> pegarFilmes(){
        List<Filme> filmes = new ArrayList<>();
        Filme filme = new Filme();
        Ator ator1 = new Ator();
        ator1.setId(11);
        ator1.setNome("Paulo");
        Ator ator2 = new Ator();
        ator2.setId(22);
        ator2.setNome("Ana");
        filme.setAtores(Arrays.asList(ator1, ator2));
        filmes.add(filme);
        return filmes;
    }


    @Before
    public void inicializar(){
        this.atorRepository = Mockito.spy(AtorRepository.class);
        this.filmeRepository = Mockito.spy(FilmeRepository.class);
        this.listaAtores = criarDadosAtores();
        this.listaFilmes = pegarFilmes();

        Mockito.doReturn(listaAtores.get(0)).when(atorRepository).findById((long)10);
        Mockito.doReturn(listaFilmes).when(filmeRepository).findAll();

        this.pessoaService = new PessoaService();
        Whitebox.setInternalState(pessoaService, "atorRepository", atorRepository);
        Whitebox.setInternalState(pessoaService, "filmeRepository", filmeRepository);
    }

    @Test
    public void atualizarAtor(){
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setAltura("1.75");
        pessoaDTO.setCidadeNatal("Recife");
        pessoaDTO.setGenero("masculino");
        pessoaDTO.setNome("João");
        pessoaDTO.setPaisOndeMora("Brasil");

        this.pessoaService.atualizarAtor(10, pessoaDTO);

        Pessoa atorAtualizado = this.listaAtores.get(0);
        Assert.assertEquals(10, atorAtualizado.getId().intValue());
        Assert.assertEquals("Recife", atorAtualizado.getCidadeNatal());
        Assert.assertEquals("1.75", atorAtualizado.getAltura());
        Assert.assertEquals("João", atorAtualizado.getNome());
        Assert.assertEquals("masculino", atorAtualizado.getGenero());
        Assert.assertEquals("Brasil", atorAtualizado.getPaisOndeMora());
    }

    @Test
    public void deletarAtor(){
        Assert.assertEquals(2, this.listaFilmes.get(0).getAtores().size());
        Assert.assertEquals(11, this.listaFilmes.get(0).getAtores().get(0).getId().intValue());
        this.pessoaService.deleteAtor(11);

    }

}

package com.movie.pitang.controllers;

import com.movie.pitang.error.ResourceNotFoundException;
import com.movie.pitang.models.Ator;
import com.movie.pitang.models.Filme;
import com.movie.pitang.models.dto.FilmeDTO;
import com.movie.pitang.repositories.FilmeRepository;
import com.movie.pitang.services.FilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/filmes")
@CrossOrigin("http://localhost:4200")
public class FilmeController {

    @Autowired
    private FilmeRepository filmeRepository;

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private InitFilme initFilme;

    private List<Ator> atores;

    public FilmeRepository getSerieRepository() {
        return filmeRepository;
    }

    public void setSerieRepository(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){

        Page<Filme> filmes =  this.filmeRepository.findAll(pageable);

        return new ResponseEntity<>(filmes, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getFilmeById(@PathVariable long id){
        if(!this.filmeRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe filme com esse ID");
        }

        Optional<Filme> filme = this.filmeRepository.findById(id);
        return new ResponseEntity<>(filme.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/filtro/")
    public ResponseEntity<?> getFilmeByFields( Pageable pageable,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam(value = "lingua", required = false) String lingua
    ){
        List<Filme> lista = new ArrayList<>();

        if(titulo == null && ano == null && lingua == null){
            return new ResponseEntity<>(this.filmeRepository.findAll(), HttpStatus.OK);
        }
        if(titulo != null){
//            lista.addAll(this.filmeRepository.findByTituloContainingIgnoreCase(titulo));
            Page<Filme> pageFilmes = this.filmeRepository.findByTituloContainingIgnoreCase(titulo, pageable);
            return new ResponseEntity<>(pageFilmes, HttpStatus.OK);
        }
        if(ano != null){
//            lista.addAll(this.filmeRepository.findByAnoLancamentoLike(ano));
            Page<Filme> pageFilmes = this.filmeRepository.findByAnoLancamentoLike(ano, pageable);
            return new ResponseEntity<>(pageFilmes, HttpStatus.OK);
        }
        if(lingua != null){
//            lista.addAll(this.filmeRepository.findByLinguaLike(lingua));
            Page<Filme> pageFilmes = this.filmeRepository.findByLinguaLike(lingua, pageable);
            return new ResponseEntity<>(pageFilmes, HttpStatus.OK);
        }

        if(lista.size() == 0){
            throw new ResourceNotFoundException("Não existe nenhum filme cadastrado com alguma dessas especificações");
        }

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

//    @PostMapping
//    @Transactional(rollbackFor = Exception.class)
//    public ResponseEntity<?> createFilme(@Valid @RequestBody FilmeDTO filmedto){
//
//        Filme filme = this.filmeService.criar(filmedto);
//
//        this.filmeRepository.save(filme);
//
//        return new ResponseEntity<>(filme, HttpStatus.CREATED);
//    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(){

        this.filmeRepository.deleteAll();

        return new ResponseEntity<>("Todos os filmes foram deletados", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFilmeById(@PathVariable long id){
        try{
            this.filmeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException ex){
            return new ResponseEntity<>("Não  existe filme com esse ID",HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateFilmeById(@PathVariable long id, @RequestBody FilmeDTO filmedto){
        if(!this.filmeRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe filme com esse ID");
        }

        Filme filme = filmeService.atualizar(id, filmedto);

        this.filmeRepository.save(filme);
        return new ResponseEntity<>(filme, HttpStatus.OK);
    }

    @PostMapping(value = "/inicializar")
    public ResponseEntity<?> pegarFilmesAPI(){
        List<Filme> lista = initFilme.listarFilmes();
        for(Filme filme : lista){
            this.filmeRepository.save(filme);
        }
        //this.filmeRepository.saveAll(lista);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

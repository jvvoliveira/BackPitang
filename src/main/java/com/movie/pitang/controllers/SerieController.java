package com.movie.pitang.controllers;

import com.movie.pitang.error.ResourceNotFoundException;
import com.movie.pitang.models.Serie;
import com.movie.pitang.models.dto.SerieDTO;
import com.movie.pitang.repositories.SerieRepository;
import com.movie.pitang.services.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/series")
@CrossOrigin("http://localhost:4200")
public class SerieController {

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private InitSerie initSerie;

    @Autowired
    private SerieService serieService;

    public SerieRepository getSerieRepository() {
        return serieRepository;
    }

    public void setSerieRepository(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){

        Page<Serie> series =  this.serieRepository.findAll(pageable);

        return new ResponseEntity<>(series, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSerieById(@PathVariable long id){
        if(!this.serieRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe serie com esse ID");
        }

        Optional<Serie> serie = this.serieRepository.findById(id);
        return new ResponseEntity<>(serie.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/filtro/")
    public ResponseEntity<?> getSerieByFields(Pageable pageable,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam(value = "lingua", required = false) String lingua
    ){
        List<Serie> lista = new ArrayList<>();

        if(titulo == null && ano == null && lingua == null){
            return new ResponseEntity<>(this.serieRepository.findAll(), HttpStatus.OK);
        }
        if(titulo != null){
            Page<Serie> pageSeries = this.serieRepository.findByTituloContainingIgnoreCase(titulo, pageable);
            return new ResponseEntity<>(pageSeries, HttpStatus.OK);
        }
        if(ano != null){
            Page<Serie> pageSeries = this.serieRepository.findByAnoLancamentoLike(ano, pageable);
            return new ResponseEntity<>(pageSeries, HttpStatus.OK);
        }
        if(lingua != null){
            Page<Serie> pageSeries = this.serieRepository.findByLinguaLike(lingua, pageable);
            return new ResponseEntity<>(pageSeries, HttpStatus.OK);
        }

        if(lista.size() == 0){
            throw new ResourceNotFoundException("Não existe nenhuma série cadastrada com alguma dessas especificações");
        }

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createSerie(@Valid @RequestBody Serie serie){

        Serie serieSalva = this.serieRepository.save(serie);

        return new ResponseEntity<>(serieSalva, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(){

        this.serieRepository.deleteAll();

        return new ResponseEntity<>("Todas as séries foram deletadas", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteSerieById(@PathVariable long id){
        try{
            this.serieRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException ex){
            return new ResponseEntity<>("Não  existe série com esse ID",HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateSerieById(@PathVariable long id, @RequestBody SerieDTO seriedto){

        if(!this.serieRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe série com esse ID");
        }

        Serie serie = this.serieService.atualizar(id, seriedto);

        this.serieRepository.save(serie);
        return new ResponseEntity<>(serie, HttpStatus.OK);
    }

    @PostMapping(value = "/inicializar")
    public ResponseEntity<?> pegarSeriesAPI(){
        this.serieRepository.saveAll(initSerie.listarSeries());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

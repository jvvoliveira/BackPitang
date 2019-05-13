package com.movie.pitang.controllers;

import com.movie.pitang.error.ResourceNotFoundException;
import com.movie.pitang.models.Genero;
import com.movie.pitang.repositories.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/generos")
@CrossOrigin("http://localhost:4200")
public class GeneroController {

    @Autowired
    private GeneroRepository generoRepository;

    public GeneroRepository getGeneroRepository() {
        return generoRepository;
    }

    public void setGeneroRepository(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){

        Page<Genero> generos =  this.generoRepository.findAll(pageable);

        return new ResponseEntity<>(generos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getGeneroById(@PathVariable long id){
        if(!this.generoRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe gênero com esse ID");
        }

        Optional<Genero> genero = this.generoRepository.findById(id);
        return new ResponseEntity<>(genero.get(), HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createGenero(@Valid @RequestBody Genero genero){

        Genero generoSalvo = this.generoRepository.save(genero);

        return new ResponseEntity<>(generoSalvo, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(){

        this.generoRepository.deleteAll();

        return new ResponseEntity<>("Todos os gêneros foram deletados", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteGeneroById(@PathVariable long id){
        try{
            this.generoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException ex){
            return new ResponseEntity<>("Não  existe gênero com esse ID",HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateGeneroById(@PathVariable long id, @RequestBody Genero genero){

        if(!this.generoRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe gênero com esse ID");
        }

        Optional<Genero> opGenero = this.generoRepository.findById(id);

        if(genero.getDescricao() != null){
            opGenero.get().setDescricao(genero.getDescricao());
        }

        this.generoRepository.save(opGenero.get());
        return new ResponseEntity<>(opGenero.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/inicializar")
    public ResponseEntity<?> pegarGenerosAPI(){
        InitGenero initGenero = new InitGenero();
        this.generoRepository.saveAll(initGenero.listarGeneros());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

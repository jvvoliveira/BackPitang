package com.movie.pitang.controllers;


import com.movie.pitang.error.ResourceNotFoundException;
import com.movie.pitang.models.Ator;
import com.movie.pitang.models.dto.PessoaDTO;
import com.movie.pitang.repositories.AtorRepository;
import com.movie.pitang.services.PessoaService;
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

@RestController
@RequestMapping("/atores")
@CrossOrigin("http://localhost:4200")
public class AtorController {

    @Autowired
    private AtorRepository atorRepository;

    @Autowired
    private PessoaService pessoaService; //transformação de PessoaDTO para Pessoa (atualizar)

    public AtorRepository getAtorRepository() {
        return atorRepository;
    }

    public void setAtorRepository(AtorRepository atorRepository) {
        this.atorRepository = atorRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){

        Page<Ator> atores =  this.atorRepository.findAll(pageable);

        return new ResponseEntity<>(atores, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAtorById(@PathVariable long id){
        if(!this.atorRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe ator/atriz com esse ID");
        }

        Ator ator = this.atorRepository.findById(id);
        return new ResponseEntity<>(ator, HttpStatus.OK);
    }

    @GetMapping(value = "/filtro/")
    public ResponseEntity<?> getAtorByFields( Pageable pageable,
            @RequestParam(value = "nome", required = false) String nome
    ){
        List<Ator> lista = new ArrayList<>();

        if(nome == null){
            return new ResponseEntity<>(this.atorRepository.findAll(), HttpStatus.OK);
        }
        if(nome != null){
            Page<Ator> pageAtores = this.atorRepository.findByNomeContainingIgnoreCase(nome, pageable);
            return new ResponseEntity<>(pageAtores, HttpStatus.OK);
        }

        if(lista.size() == 0){
            throw new ResourceNotFoundException("Não existe nenhum ator/atriz cadastrado com alguma dessas especificações");
        }

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)//garantir atomicidade da transação
    public ResponseEntity<?> createAtor(@Valid @RequestBody Ator ator){

        Ator atorSalvo = this.atorRepository.save(ator);

        return new ResponseEntity<>(atorSalvo, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(){

        this.atorRepository.deleteAll();

        return new ResponseEntity<>("Todos os atores foram deletados", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAtorById(@PathVariable long id){
        try{
            this.pessoaService.deleteAtor(id);
            this.atorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException ex){
            return new ResponseEntity<>("Não  existe ator/atriz com esse ID",HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateAtorById(@PathVariable long id, @RequestBody PessoaDTO pessoadto){

        if(!this.atorRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe ator/atriz com esse ID");
        }

        Ator ator = this.pessoaService.atualizarAtor(id, pessoadto);

        this.atorRepository.save(ator);
        return new ResponseEntity<>(ator, HttpStatus.OK);
    }
}

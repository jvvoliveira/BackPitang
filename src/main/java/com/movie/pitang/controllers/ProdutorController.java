package com.movie.pitang.controllers;

import com.movie.pitang.error.ResourceNotFoundException;
import com.movie.pitang.models.Produtor;
import com.movie.pitang.models.dto.PessoaDTO;
import com.movie.pitang.repositories.ProdutorRepository;
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
import java.util.Optional;

@RestController
@RequestMapping("/produtores")
@CrossOrigin("http://localhost:4200")
public class ProdutorController {
    @Autowired
    private ProdutorRepository produtorRepository;

    @Autowired
    private PessoaService pessoaService;

    public ProdutorRepository getProdutorRepository() {
        return produtorRepository;
    }

    public void setProdutorRepository(ProdutorRepository produtorRepository) {
        this.produtorRepository = produtorRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){

        Page<Produtor> produtores =  this.produtorRepository.findAll(pageable);

        return new ResponseEntity<>(produtores, HttpStatus.OK);
    }

    @GetMapping("/autores")
    public ResponseEntity<?> getAutores(Pageable pageable){
        Page<Produtor> autores = this.produtorRepository.findByTipoLike("Writing", pageable);

        return new ResponseEntity<>(autores, HttpStatus.OK);
    }

    @GetMapping("/diretores")
    public ResponseEntity<?> getDiretores(Pageable pageable){
        Page<Produtor> diretores = this.produtorRepository.findByTipoLike("Directing", pageable);

        return new ResponseEntity<>(diretores, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getProdutorById(@PathVariable long id){
        if(!this.produtorRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe produtor com esse ID");
        }

        Optional<Produtor> produtor = this.produtorRepository.findById(id);
        return new ResponseEntity<>(produtor.get(), HttpStatus.OK);
    }

    @GetMapping(value = "autores/filtro/")
    public ResponseEntity<?> getAutorByFields( Pageable pageable,
            @RequestParam(value = "nome", required = false) String nome
    ){
        List<Produtor> lista = new ArrayList<>();

        if(nome == null){
            return new ResponseEntity<>
                    (this.produtorRepository.findByTipoLike("Writing", pageable), HttpStatus.OK);
        }
        if(nome != null){
            Page<Produtor> pageAutores = this.produtorRepository.
                    findByNomeContainingIgnoreCaseAndTipoLike(nome, "Writing", pageable);
            return new ResponseEntity<>(pageAutores, HttpStatus.OK);
        }

        if(lista.size() == 0){
            throw new ResourceNotFoundException("Não existe nenhum autor cadastrado com alguma dessas especificações");
        }

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping(value = "diretores/filtro/")
    public ResponseEntity<?> getDiretorByFields( Pageable pageable,
                                                 @RequestParam(value = "nome", required = false) String nome
    ){
        List<Produtor> lista = new ArrayList<>();

        if(nome == null){
            return new ResponseEntity<>
                    (this.produtorRepository.findByTipoLike("Directing", pageable), HttpStatus.OK);
        }

        if(nome != null){
            Page<Produtor> pageDiretores = this.produtorRepository.
                    findByNomeContainingIgnoreCaseAndTipoLike(nome, "Directing", pageable);
            return new ResponseEntity<>(pageDiretores, HttpStatus.OK);
        }

        if(lista.size() == 0){
            throw new ResourceNotFoundException("Não existe nenhum diretor cadastrado com alguma dessas especificações");
        }

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)//garantir atomicidade da transação
    public ResponseEntity<?> createProdutor(@Valid @RequestBody Produtor produtor){

        Produtor produtorSalvo = this.produtorRepository.save(produtor);

        return new ResponseEntity<>(produtorSalvo, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(){

        this.produtorRepository.deleteAll();

        return new ResponseEntity<>("Todos os produtores foram deletados", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteProdutorById(@PathVariable long id){
        try{
            this.pessoaService.deleteProdutor(id);
            this.produtorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException ex){
            return new ResponseEntity<>("Não existe produtor com esse ID",HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateProdutorById(@PathVariable long id, @RequestBody PessoaDTO pessoadto){

        if(!this.produtorRepository.existsById(id)){
            throw new ResourceNotFoundException("Não existe produtor com esse ID");
        }

        Produtor produtor = this.pessoaService.atualizarProdutor(id, pessoadto);

        this.produtorRepository.save(produtor);
        return new ResponseEntity<>(produtor, HttpStatus.OK);
    }
}

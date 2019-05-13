package com.movie.pitang.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_ator")
@JsonIgnoreProperties({"programas"})
public class Ator extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(mappedBy = "atores", targetEntity = Programa.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Programa> programas;

    public Ator() {
    }


    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public Set<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(Set<Programa> programas) {
        this.programas = programas;
    }

    @Override
    public Long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }
}

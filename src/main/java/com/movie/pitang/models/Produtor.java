package com.movie.pitang.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_produtor")
@JsonIgnoreProperties({"programas"})
public class Produtor extends Pessoa{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(mappedBy = "produtores", targetEntity = Programa.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Programa> programas;

    @Column(name = "pro_cl_tipo")
    private String tipo;

    public Produtor() {
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Set<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(Set<Programa> programas) {
        this.programas = programas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

package com.movie.pitang.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.movie.pitang.infraestrutura.IObjectPersistent;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_genero")
@JsonIgnoreProperties({"programas"})
public class Genero implements IObjectPersistent<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "gen_cl_idAPI")
    private long idapi;

    @Column(name = "gen_cl_descricao")
    private String descricao;

    @ManyToMany(mappedBy = "generos", targetEntity = Programa.class)
    private Set<Programa> programas;

    public Genero() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genero genero = (Genero) o;

        return id == genero.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdapi() {
        return idapi;
    }

    public void setIdapi(long idapi) {
        this.idapi = idapi;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Programa> getProgramas() {
        return programas;
    }

    public void setProgramas(Set<Programa> programas) {
        this.programas = programas;
    }

}

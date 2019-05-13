package com.movie.pitang.models;

import com.movie.pitang.infraestrutura.IObjectPersistent;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Pessoa implements IObjectPersistent<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "pes_cl_idAPI")
    private long idapi;

    @Column(name = "pes_cl_nome")
    private String nome;

    @Column(name = "pes_cl_altura")
    private String altura;

    @Column(name = "pes_cl_genero")
    private String genero;

    @Column(name = "pes_cl_cidadeNatal")
    private String cidadeNatal;

    @Column(name = "pes_cl_paisOndeMora")
    private String paisOndeMora;

    @Column(name = "pes_cl_poster")
    private String poster;

    public Pessoa() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pessoa pessoa = (Pessoa) o;

        return idapi == pessoa.idapi;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getPaisOndeMora() {
        return paisOndeMora;
    }

    public void setPaisOndeMora(String paisOndeMora) {
        this.paisOndeMora = paisOndeMora;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCidadeNatal() {
        return cidadeNatal;
    }

    public void setCidadeNatal(String cidadeNatal) {
        this.cidadeNatal = cidadeNatal;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}

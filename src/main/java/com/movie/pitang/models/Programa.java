package com.movie.pitang.models;

import com.movie.pitang.infraestrutura.IObjectPersistent;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Programa implements IObjectPersistent<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "pro_cl_idAPI", unique = true)
    private long idapi;

    @Column(name = "pro_cl_titulo")
    private String titulo;

    @Size(message = "descricao maior que o permitido")
    @Column(name = "pro_cl_descricao", length = 2000)
    private String descricao;

    @Column(name = "pro_cl_paisDeOrigem")
    private String paisDeOrigem;

    @Column(name = "pro_cl_lingua")
    private String lingua;

    @Column(name = "pro_cl_anoLancamento")
    private Integer anoLancamento;

    @Column(name = "pro_cl_duracao")
    private Long duracao;

    @Column(name = "pro_cl_poster")
    private String poster;

    @Column(name= "pro_cl_imagem")
    private String imagem;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "programa_genero",joinColumns = @JoinColumn(name = "pro_cl_id"),
            inverseJoinColumns = @JoinColumn(name = "gen_cl_id"))
    private List<Genero> generos;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "programa_atores",joinColumns = @JoinColumn(name = "pro_cl_id"),
            inverseJoinColumns = @JoinColumn(name = "ato_cl_id"))
    private List<Ator> atores;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "programa_produtores",joinColumns = @JoinColumn(name = "prog_cl_id"),
            inverseJoinColumns = @JoinColumn(name = "prod_cl_id"))
    private List<Produtor> produtores;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Programa programa = (Programa) o;

        return id == programa.id;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPaisDeOrigem() {
        return paisDeOrigem;
    }

    public void setPaisDeOrigem(String paisDeOrigem) {
        this.paisDeOrigem = paisDeOrigem;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public Long getDuracao() {
        return duracao;
    }

    public void setDuracao(long duracao) {
        this.duracao = duracao;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public void setDuracao(Long duracao) {
        this.duracao = duracao;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public List<Ator> getAtores() {
        return atores;
    }

    public void setAtores(List<Ator> atores) {
        this.atores = atores;
    }

    public List<Produtor> getProdutores() {
        return produtores;
    }

    public void setProdutores(List<Produtor> produtores) {
        this.produtores = produtores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}

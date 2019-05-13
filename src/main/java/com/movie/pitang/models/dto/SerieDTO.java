package com.movie.pitang.models.dto;

import java.util.Set;

public class SerieDTO {
    private String titulo;
    private String descricao;
    private String paisDeOrigem;
    private String lingua;
    private Integer anoLancamento;
    private Long duracao;
    private Integer temporadas;
    private Set<String> generos;
    private Set<String> atores;
    private Set<String> produtores;


    public SerieDTO(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerieDTO serieDTO = (SerieDTO) o;

        return titulo == serieDTO.titulo;
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

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
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

    public void setDuracao(Long duracao) {
        this.duracao = duracao;
    }

    public Set<String> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<String> generos) {
        this.generos = generos;
    }

    public Set<String> getAtores() {
        return atores;
    }

    public void setAtores(Set<String> atores) {
        this.atores = atores;
    }

    public Set<String> getProdutores() {
        return produtores;
    }

    public void setProdutores(Set<String> produtores) {
        this.produtores = produtores;
    }

    public Integer getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(Integer temporadas) {
        this.temporadas = temporadas;
    }
}

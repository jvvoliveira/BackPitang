package com.movie.pitang.models.dto;

public class PessoaDTO {
    private String nome;
    private String altura;
    private String genero;
    private String cidadeNatal;
    private String paisOndeMora;
    private String tipo;

    public PessoaDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PessoaDTO pessoaDTO = (PessoaDTO) o;

        return nome != null ? nome.equals(pessoaDTO.nome) : pessoaDTO.nome == null;
    }

    @Override
    public int hashCode() {
        return nome != null ? nome.hashCode() : 0;
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

    public String getPaisOndeMora() {
        return paisOndeMora;
    }

    public void setPaisOndeMora(String paisOndeMora) {
        this.paisOndeMora = paisOndeMora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

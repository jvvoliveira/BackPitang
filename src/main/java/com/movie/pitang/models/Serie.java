package com.movie.pitang.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_serie")
public class Serie extends Programa{


    @Column(name = "ser_cl_temporadas")
    private Integer temporadas;

    public Serie() {

    }


    public Integer getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }
}

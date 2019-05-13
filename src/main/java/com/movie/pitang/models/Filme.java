package com.movie.pitang.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_filme")
public class Filme extends Programa {

    public Filme() {
    }

}

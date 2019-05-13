package com.movie.pitang.models.api;

import com.movie.pitang.models.results.GeneroResult;

import java.util.List;

public class GeneroAPI {
    private List<GeneroResult> genres;

    public GeneroAPI(){

    }

    public List<GeneroResult> getGenres() {
        return genres;
    }

    public void setGenres(List<GeneroResult> genres) {
        this.genres = genres;
    }
}

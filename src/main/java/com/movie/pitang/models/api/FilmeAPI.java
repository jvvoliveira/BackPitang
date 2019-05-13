package com.movie.pitang.models.api;

import com.movie.pitang.models.results.FilmeResult;

import java.util.List;

public class FilmeAPI extends ProgramaAPI{

    private List<FilmeResult> results;

    public FilmeAPI() {
    }

    public List<FilmeResult> getResults() {
        return results;
    }

    public void setResults(List<FilmeResult> results) {
        this.results = results;
    }

}

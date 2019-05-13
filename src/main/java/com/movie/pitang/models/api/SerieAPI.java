package com.movie.pitang.models.api;

import com.movie.pitang.models.results.SerieResult;

import java.util.List;

public class SerieAPI extends ProgramaAPI{

    private List<SerieResult> results;

    public SerieAPI() {
    }

    public List<SerieResult> getResults() {
        return results;
    }

    public void setResults(List<SerieResult> results) {
        this.results = results;
    }
}

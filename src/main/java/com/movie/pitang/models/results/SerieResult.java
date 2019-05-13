package com.movie.pitang.models.results;

import java.util.Set;

public class SerieResult extends ProgramaResult{
    private String first_air_date;
    private Set<String> origin_country;
    private String name;
    private String original_name;

    public SerieResult() {
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public Set<String> getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(Set<String> origin_country) {
        this.origin_country = origin_country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }
}

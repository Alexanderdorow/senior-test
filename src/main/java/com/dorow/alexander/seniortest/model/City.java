package com.dorow.alexander.seniortest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class City {
    @Field("ibge_id")
    private String ibgeId;
    private String uf;
    private String name;
    private boolean capital;
    private double lon;
    private double lat;
    @Field("no_accents")
    private String noAccents;
    @Field("alternative_names")
    private String alternativeNames;
    private String microregion;
    private String mesoregion;

    public City() {
    }


    public City(String ibgeId, String uf, String name, boolean capital, double lon, double lat, String noAccents, String alternativeNames, String microregion, String mesoregion) {
        this.ibgeId = ibgeId;
        this.uf = uf;
        this.name = name;
        this.capital = capital;
        this.lon = lon;
        this.lat = lat;
        this.noAccents = noAccents;
        this.alternativeNames = alternativeNames;
        this.microregion = microregion;
        this.mesoregion = mesoregion;
    }

    public String getIbgeId() {
        return ibgeId;
    }

    public void setIbgeId(String ibgeId) {
        this.ibgeId = ibgeId;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getNoAccents() {
        return noAccents;
    }

    public void setNoAccents(String noAccents) {
        this.noAccents = noAccents;
    }

    public String getAlternativeNames() {
        return alternativeNames;
    }

    public void setAlternativeNames(String alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public String getMicroregion() {
        return microregion;
    }

    public void setMicroregion(String microregion) {
        this.microregion = microregion;
    }

    public String getMesoregion() {
        return mesoregion;
    }

    public void setMesoregion(String mesoregion) {
        this.mesoregion = mesoregion;
    }
}

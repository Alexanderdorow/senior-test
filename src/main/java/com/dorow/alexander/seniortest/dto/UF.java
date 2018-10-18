package com.dorow.alexander.seniortest.dto;

import org.springframework.data.mongodb.core.mapping.Field;

public class UF {

    @Field("_id")
    private String name;
    @Field("count")
    private int cityCount;

    public UF() {
    }

    public UF(String name, int count) {
        this.name = name;
        this.cityCount = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityCount() {
        return cityCount;
    }

    public void setCityCount(int cityCount) {
        this.cityCount = cityCount;
    }
}

package com.dmustt.mdewantara.floor.schema;

/**
 * Created by mdewantara on 7/23/17.
 */

public class FloorSchema {
    private int id, capacity, stadiumId;
    private String name;

    public FloorSchema(int id, String name, int capacity, int stadium){
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.stadiumId = stadium;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(Integer stadiumId) {
        this.stadiumId = stadiumId;
    }


}

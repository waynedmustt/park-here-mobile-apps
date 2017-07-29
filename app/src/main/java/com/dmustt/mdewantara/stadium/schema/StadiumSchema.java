package com.dmustt.mdewantara.stadium.schema;

/**
 * Created by mdewantara on 7/20/17.
 */

public class StadiumSchema {
    private int id, capacity;
    private String name, address, image;
    public static final String DMUSTT_TAG = "the_dmustt_message";

    public StadiumSchema(int id, String name, String address, String image, int capacity){
        this.id = id;
        this.name = name;
        this.address = address;
        this.image = image;
        this.capacity = capacity;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setImage(Integer capacity) {
        this.capacity = capacity;
    }


}

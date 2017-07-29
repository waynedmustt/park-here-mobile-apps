package com.dmustt.mdewantara.parking_area.schema;

/**
 * Created by mdewantara on 7/29/17.
 */

public class ParkingAreaSchema {
    private int id, floorId;
    private String parkingAreaCode;

    public ParkingAreaSchema(int id, String parkingAreaCode, int floorId) {
        this.id = id;
        this.parkingAreaCode = parkingAreaCode;
        this.floorId = floorId;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getParkingAreaCode() {return parkingAreaCode;}

    public void setParkingAreaCode(String parkingAreaCode) {this.parkingAreaCode = parkingAreaCode;}

    public int getFloorId() {return floorId;}

    public void setFloorId(int floorId) {this.floorId = floorId;}
}

package com.huawei;

public class ArrivalTime {
    private String carID;
    private int arrivalTime;

    public ArrivalTime(String carID, int arrivalTime) {
        this.carID = carID;
        this.arrivalTime = arrivalTime;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}

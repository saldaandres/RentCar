package com.example.rentcar;

public class Car {
    private String platenumber;
    private String brand;
    private int state;
    private int dailyvalue;

    public Car() {
    }

    public Car(String plateNumber, String brand, int state, int dailyValue) {
        this.platenumber = plateNumber;
        this.brand = brand;
        this.state = state;
        this.dailyvalue = dailyValue;
    }

    public String getPlatenumber() {
        return platenumber;
    }

    public void setPlatenumber(String platenumber) {
        this.platenumber = platenumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDailyvalue() {
        return dailyvalue;
    }

    public void setDailyvalue(int dailyvalue) {
        this.dailyvalue = dailyvalue;
    }
}

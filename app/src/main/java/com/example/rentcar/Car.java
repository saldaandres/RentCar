package com.example.rentcar;

public class Car {
    private String plateNumber;
    private String brand;
    private int state;
    private int dailyValue;

    public Car() {
    }

    public Car(String plateNumber, String brand, int state, int dailyValue) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.state = state;
        this.dailyValue = dailyValue;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
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

    public int getDailyValue() {
        return dailyValue;
    }

    public void setDailyValue(int dailyValue) {
        this.dailyValue = dailyValue;
    }
}

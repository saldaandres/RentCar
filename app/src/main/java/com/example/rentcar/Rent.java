package com.example.rentcar;

public class Rent {
    private int rentNumber;
    private String userName;
    private String plateNumber;
    private String initialDate;
    private String finalDate;
    private int status;

    public Rent() {
    }

    public Rent(int rentNumber, String userName, String plateNumber, String initialDate, String finalDate, int status) {
        this.rentNumber = rentNumber;
        this.userName = userName;
        this.plateNumber = plateNumber;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.status = status;
    }

    public int getRentNumber() {
        return rentNumber;
    }

    public void setRentNumber(int rentNumber) {
        this.rentNumber = rentNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

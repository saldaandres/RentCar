package com.example.rentcar;

public class Rent {
    private long rentNumber;
    private String email;
    private String plateNumber;
    private String initialDate;
    private String finalDate;
    private int status;

    public Rent() {
    }

    public Rent(long rentNumber, String email, String plateNumber, String initialDate, String finalDate, int status) {
        this.rentNumber = rentNumber;
        this.email = email;
        this.plateNumber = plateNumber;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.status = status;
    }

    public long getRentNumber() {
        return rentNumber;
    }

    public void setRentNumber(long rentNumber) {
        this.rentNumber = rentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

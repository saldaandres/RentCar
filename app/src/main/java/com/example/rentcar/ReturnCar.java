package com.example.rentcar;

public class ReturnCar {
    private int returnNumber;
    private int rentNumber;
    private String returnDate;

    public ReturnCar() {
    }

    public ReturnCar(int returnNumber, int rentNumber, String returnDate) {
        this.returnNumber = returnNumber;
        this.rentNumber = rentNumber;
        this.returnDate = returnDate;
    }

    public int getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(int returnNumber) {
        this.returnNumber = returnNumber;
    }

    public int getRentNumber() {
        return rentNumber;
    }

    public void setRentNumber(int rentNumber) {
        this.rentNumber = rentNumber;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}

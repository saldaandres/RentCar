package com.example.rentcar;

public class Return {
    private long returnNumber;
    private long rentNumber;
    private String returnDate;

    public Return() {
    }

    public Return(long returnNumber, long rentNumber, String returnDate) {
        this.returnNumber = returnNumber;
        this.rentNumber = rentNumber;
        this.returnDate = returnDate;
    }

    public long getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(int returnNumber) {
        this.returnNumber = returnNumber;
    }

    public long getRentNumber() {
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

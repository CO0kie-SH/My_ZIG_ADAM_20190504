package com.nle.mylibrary.forUse.exeception;

public class SerialException extends Exception {
    private String describe;

    public SerialException(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return super.toString() + " " + describe;
    }
}

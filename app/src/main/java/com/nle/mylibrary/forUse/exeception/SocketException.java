package com.nle.mylibrary.forUse.exeception;

public class SocketException extends Exception {
    private String describe;

    public SocketException(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return super.toString() + " " + describe;
    }
}

package com.nle.mylibrary.enums;

public enum BaudPair {
    BAUD_1200(1200, 0), BAUD_2400(2400, 1), BAUD_4800(4800, 2), BAUD_9600(9600, 3), BAUD_19200(19200, 4), BAUD_38400(38400, 5), BAUD_57600(57600, 6), BAUD_115200(115200, 7),
    BAUD_230400(230400, 8), BAUD_921600(921600, 9);

    private int baudNum;
    private int pair;

    BaudPair(int baudNum, int pair) {
        this.baudNum = baudNum;
        this.pair = pair;
    }

    public int getBaudNum() {
        return baudNum;
    }

    public int getPair() {
        return pair;
    }

    public static int getPairByBaud(String baud) {
        int baudInt = Integer.parseInt(baud);
        for (BaudPair baudPair : BaudPair.values()) {
            if (baudPair.getBaudNum() == baudInt) {
                return baudPair.getPair();
            }
        }
        return -1;
    }

}

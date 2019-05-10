package com.nle.mylibrary.enums.led;

public enum DisplayColor {
    RED("红色", (byte) 0x00),
    GREEN("绿色", (byte) 0x01),
    YELLOW("黄色", (byte) 0x02);
    private String des;
    private byte code;

    DisplayColor(String des, byte code) {
        this.des = des;
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public byte getCode() {
        return code;
    }
}

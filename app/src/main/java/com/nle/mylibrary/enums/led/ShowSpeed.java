package com.nle.mylibrary.enums.led;

public enum ShowSpeed {
    SPEED1("一级", (byte) 0x00),
    SPEED2("二级", (byte) 0x01),
    SPEED3("三级", (byte) 0x02),
    SPEED4("四级", (byte) 0x03),
    SPEED5("五级", (byte) 0x04),
    SPEED6("六级", (byte) 0x05),
    SPEED7("七级", (byte) 0x06),
    SPEED8("八级", (byte) 0x07);


    private String des;
    private byte code;

     ShowSpeed(String des, byte code) {
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

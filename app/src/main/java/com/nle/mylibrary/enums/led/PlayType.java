package com.nle.mylibrary.enums.led;

public enum PlayType {
    LEFT((byte) 0x01, "左移"),
    UP((byte) 0x02, "上移"),
    DOWN((byte) 0x03, "下移"),
    DOWN_OVER((byte) 0x04, "下覆盖"),
    UP_OVER((byte) 0x05, "上覆盖"),
    WHITE((byte) 0x06, "翻白显示"),
    SPANGLE((byte) 0x07, "闪烁显示 "),
    NOW((byte) 0x08, "立即打出");

    private byte code;
    private String des;

    PlayType(byte code, String des) {
        this.code = code;
        this.des = des;
    }

    public byte getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}

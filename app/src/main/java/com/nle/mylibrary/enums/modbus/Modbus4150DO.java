package com.nle.mylibrary.enums.modbus;

public enum Modbus4150DO {
    DO0((byte) 0x10, "DO0"),
    DO1((byte) 0x11, "DO1"),
    DO2((byte) 0x12, "DO2"),
    DO3((byte) 0x13, "DO3"),
    DO4((byte) 0x14, "DO4"),
    DO5((byte) 0x15, "DO5"),
    DO6((byte) 0x16, "DO6"),
    DO7((byte) 0x17, "DO7");
    private byte address;
    private String displayName;

    Modbus4150DO(byte address, String displayName) {
        this.address = address;
        this.displayName = displayName;
    }

    public byte getAddress() {
        return address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String getDisplayNameByAdd(byte address) {
        for (Modbus4150DO modbus4150DO : Modbus4150DO.values()) {
            if (modbus4150DO.address == address) {
                return modbus4150DO.displayName;
            }
        }
        return "";
    }
}

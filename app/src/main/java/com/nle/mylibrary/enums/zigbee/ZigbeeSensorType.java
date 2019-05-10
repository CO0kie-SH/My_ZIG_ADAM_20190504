package com.nle.mylibrary.enums.zigbee;

public enum ZigbeeSensorType {
    TEM_HUM("温湿度传感器", 0x01),
    WEIGHT("重力传感器", 0x02),
    GYRO("陀螺仪", 0x03),
    PERSON("人体传感器", 0x11),
    LIGHT("光照传感器", 0x21),
    CO("CO传感器(空气质量)", 0x22),
    COM_GAS("可燃气传感器", 0x23),
    FIRE("火焰传感器", 0x24),
    ALCOHOL("酒精", 0x25),
    FOUR_ENTER("四通道电流", 0x30),;
    private String name;
    private int code;


    ZigbeeSensorType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static ZigbeeSensorType getSensorTypeByCode(int code) {
        for (ZigbeeSensorType sensorType : ZigbeeSensorType.values()) {
            if (sensorType.getCode() == code) {
                return sensorType;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static ZigbeeSensorType index(int index) {
        int i = 0;
        for (ZigbeeSensorType zigbeeSensorType : ZigbeeSensorType.values()) {
            if (index == i) {
                return zigbeeSensorType;
            } else {
                i++;
            }
        }
        return null;
    }
    public static int getIndex(ZigbeeSensorType sensorTypeTarget) {
        for (int i = 0; i < ZigbeeSensorType.values().length; i++) {
            ZigbeeSensorType sensorType = ZigbeeSensorType.values()[i];
            if (sensorType == sensorTypeTarget) {
                return i;
            }
        }
        return -1;
    }
}

package com.nle.mylibrary.enums.zigbee;

public enum DeviceType {

    COORDINATOR(0x00, "协调器"),
    ROUTER(0X01, "路由器"),
    FUN_NODE(0x02, "全功能节点"),
    TERMINAL_NODE(0x03, "终端节点"),
    RELAY(0x04, "继电器");

    private String typeName;
    private int deviceTypeCode;

    public String getTypeName() {
        return typeName;
    }

    public int getDeviceTypeCode() {
        return deviceTypeCode;
    }

    DeviceType(int deviceTypeCode, String deviceType) {
        this.typeName = deviceType;
        this.deviceTypeCode = deviceTypeCode;
    }

    public static DeviceType getDeviceByCode(int code) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (code == deviceType.getDeviceTypeCode()) {
                return deviceType;
            }
        }
        return null;
    }
    public static int getIndex(DeviceType deviceTypeTarget) {
        for (int i = 0; i < DeviceType.values().length; i++) {
            DeviceType deviceType = DeviceType.values()[i];
            if (deviceTypeTarget == deviceType) {
                return i;
            }
        }
        return -1;
    }
}

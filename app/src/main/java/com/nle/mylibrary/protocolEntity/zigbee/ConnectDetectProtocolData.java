package com.nle.mylibrary.protocolEntity.zigbee;


import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.enums.zigbee.DeviceType;

/**
 * 连接测试协议
 */
public class ConnectDetectProtocolData extends DataProtocol {
    private DeviceType deviceType;

    public DeviceType getDeviceType() {
        return deviceType;
    }

    @Override
    public byte[] buildRequestCommand() {
        return new byte[]{(byte) 0xE9, (byte) 0xE3, 0x04, 0x55, (byte) 0xaa, 0x66, 0x77, (byte) 0xea};
    }

    @Override
    public void receiveMsg(byte[] data, int len) {
        super.receiveMsg(data, len);
        int deviceCode = data[7] & 0xff;
        deviceType = DeviceType.getDeviceByCode(deviceCode);
    }


}

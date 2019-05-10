package com.nle.mylibrary.protocolEntity.zigbee;


import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.enums.zigbee.ZigbeeSensorType;

/**
 * 设备信息协议
 */
public class DeviceInfoProtocol extends DataProtocol {
    private int channelId;
    private int panId;
    private int serialId;
    private ZigbeeSensorType sensorType;

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public byte[] buildRequestCommand() {
        return new byte[]{(byte) 0xE9, (byte) 0xE3, 0x01, 0x02, (byte) 0x03};

    }

    public int getChannelId() {
        return channelId;
    }

    public int getPanId() {
        return panId;
    }

    public int getSerialId() {
        return serialId;
    }

    public ZigbeeSensorType getSensorType(int deviceCode) {
        if (deviceCode == 0x02) {
            return sensorType;
        }
        return null;
    }

    @Override
    public void receiveMsg(byte[] data, int len) {
        super.receiveMsg(data, len);
        byte panIdL = data[4];
        byte panIdH = data[5];
        byte channelIdByte = data[6];
        panId = DataTools.parseUnSignData(panIdH, panIdL);
        this.channelId = channelIdByte & 0xff;

        if (len == 11) {
            byte serialIdL = data[7];
            byte serialIdH = data[8];
            byte sensorTypeCode = data[9];
            this.serialId = DataTools.parseUnSignData(serialIdH, serialIdL);
            this.sensorType = ZigbeeSensorType.getSensorTypeByCode(sensorTypeCode & 0xff);
        }

    }


}

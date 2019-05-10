package com.nle.mylibrary.protocolEntity.zigbee;


import com.nle.mylibrary.protocolEntity.DataProtocol;

public class DeviceSettingProtocol extends DataProtocol {
    byte channelId;
    byte panIdL;
    byte panIdH;

    private boolean settingSuccess;

    public DeviceSettingProtocol() {
    }

    private DeviceSettingProtocol(int channelId, String panIdHexString) {
        this.channelId = (byte) channelId;
        int panId = Integer.parseInt(panIdHexString, 16);
        panIdL = (byte) (panId & 0x00ff);
        panIdH = (byte) (panId >> 8);

    }

    public void setSettingSuccess(boolean settingSuccess) {
        this.settingSuccess = settingSuccess;
    }

    public boolean isSettingSuccess() {
        return settingSuccess;
    }

    @Override
    public byte[] buildRequestCommand() {
        return null;
    }

    @Override
    public void receiveMsg(byte[] data, int len) {
        super.receiveMsg(data, len);
        try {
            settingSuccess = data[3] == 0;
        } catch (Exception ignore) {
        }
    }


    /**
     * 协调器设置协议
     */
    public static class CoordinatorSettingProtocol extends DeviceSettingProtocol {

        public CoordinatorSettingProtocol(int channelId, String panIdHexString) {
            super(channelId, panIdHexString);
        }

        @Override
        public byte[] buildRequestCommand() {
            byte[] temp = {(byte) 0xE9, (byte) 0xE3, 0x04, 0x01, panIdL, panIdH, channelId, 0x11};
            byte chk = getCHKByte(temp);
            temp[temp.length - 1] = chk;
            return temp;
        }
    }

    /**
     * 继电器设置协议
     */
    public static class RelaySettingProtocol extends DeviceSettingProtocol {
        private byte serialL;
        private byte serialH;

        public RelaySettingProtocol(int channelId, String panIdHexString, String serialIdString) {
            super(channelId, panIdHexString);
            int serialId = Integer.parseInt(serialIdString, 16);
            serialL = (byte) (serialId & 0x00ff);
            serialH = (byte) (serialId >> 8);
        }

        @Override
        public byte[] buildRequestCommand() {
            byte[] temp = {(byte) 0xE9, (byte) 0xE3, 0x06, 0x01, panIdL, panIdH, channelId, serialL, serialH, 0x01, 0x11};
            byte chk = getCHKByte(temp);
            temp[temp.length - 1] = chk;
            return temp;
        }
    }

    public static class FunNodeSettingProtocol extends DeviceSettingProtocol {
        private byte serialL;
        private byte serialH;
        private byte sensorTypeCode;

        public FunNodeSettingProtocol(int channelId, String panIdHexString, String serialIdString, int sensorTypeCode) {
            super(channelId, panIdHexString);
            int serialId = Integer.parseInt(serialIdString, 16);
            serialL = (byte) (serialId & 0x00ff);
            serialH = (byte) (serialId >> 8);
            this.sensorTypeCode = (byte) sensorTypeCode;
        }

        @Override
        public byte[] buildRequestCommand() {
            byte[] temp = {(byte) 0xE9, (byte) 0xE3, 0x06, 0x01, panIdL, panIdH, channelId, serialL, serialH, sensorTypeCode, 0x11};
            byte chk = getCHKByte(temp);
            temp[temp.length - 1] = chk;
            return temp;
        }
    }

    /**
     * 获取校验位
     *
     * @param bytes 待检测字节数组
     * @return 检验码
     */
    private static byte getCHKByte(byte[] bytes) {
        int result = bytes[2];
        for (int i = 3; i < bytes.length - 2 + 1; i++) {

            result = result ^ bytes[i];
        }
        return (byte) result;
    }
}

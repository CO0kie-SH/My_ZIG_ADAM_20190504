package com.nle.mylibrary.protocolEntity.zigbee;


import com.nle.mylibrary.protocolEntity.DataProtocol;

public class ZigBeeRelayProtocol extends DataProtocol {
    byte serialIdL;
    byte serialIdH;
    byte val0;
    byte val1;

    public ZigBeeRelayProtocol(byte serialIdL, byte serialIdH, byte val0) {
        this.serialIdL = serialIdL;
        this.serialIdH = serialIdH;
        this.val0 = val0;
    }

    public ZigBeeRelayProtocol() {

    }

    @Override
    public byte[] buildRequestCommand() {
        byte[] requestData = {(byte) 0xff, (byte) 0xf5, 0x05, 0x02, serialIdL, serialIdH, 0x00, val0, 0};
        byte lrcCode = lrcCode(requestData);
        requestData[requestData.length - 1] = lrcCode;
        return requestData;
    }


    private byte lrcCode(byte[] bytes) {
        byte result = bytes[0];
        for (int i = 1; i < bytes.length - 1; i++) {
            result = (byte) (result + bytes[i]);
        }
        result = (byte) (~result + 1);
        return result;
    }


}

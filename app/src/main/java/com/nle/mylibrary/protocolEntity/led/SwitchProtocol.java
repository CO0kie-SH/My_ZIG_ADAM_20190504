package com.nle.mylibrary.protocolEntity.led;


import com.nle.mylibrary.protocolEntity.DataProtocol;

public class SwitchProtocol extends DataProtocol {
    private byte[] command;

    @Override
    public byte[] buildRequestCommand() {
        return command;
    }

    public SwitchProtocol(int state) {
        if (state == 1) {
            command = new byte[]{(byte) 0xAA, 0x01, (byte) 0xBB, 0x4F, 0x4E};
        } else if (state == 0) {
            command = new byte[]{(byte) 0xAA, 0x01, (byte) 0xBB, 0x4F, 0x46};
        }
    }

    @Override
    public void receiveMsg(byte[] data, int len) {

    }
}

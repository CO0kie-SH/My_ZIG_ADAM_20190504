package com.nle.mylibrary.protocolEntity.led;

import com.nle.mylibrary.protocolEntity.DataProtocol;

public class LedRequestSuccessTag extends DataProtocol {
    @Override
    public byte[] buildRequestCommand() {
        return new byte[0];
    }

    @Override
    public void receiveMsg(byte[] data, int len) {

    }
}

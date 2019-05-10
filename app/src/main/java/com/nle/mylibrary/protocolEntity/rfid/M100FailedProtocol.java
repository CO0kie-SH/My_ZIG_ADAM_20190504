package com.nle.mylibrary.protocolEntity.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;

public class M100FailedProtocol extends DataProtocol
{
    private Byte errorCode;

    @Override
    public byte[] buildRequestCommand()
    {
        return new byte[0];
    }

    @Override
    public void receiveMsg(byte[] data, int len)
    {
        super.receiveMsg(data, len);
        if (data.length > 12)
            errorCode = data[12];
        else
            errorCode = null;
    }

    public static boolean isProtocalRight(byte[] data, int len){
        if (len > 9 && (data[6] + 9) == len && data[9] == (byte)0xff)
            return true;

        return false;
    }

    public Byte getErrorCode()
    {
        return errorCode;
    }
}

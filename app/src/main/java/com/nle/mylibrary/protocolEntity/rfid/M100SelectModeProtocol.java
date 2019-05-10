package com.nle.mylibrary.protocolEntity.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;

public class M100SelectModeProtocol extends DataProtocol
{
    private byte[] command;
    private boolean isSuccess;

    public M100SelectModeProtocol(){
        command = new byte[]{ (byte) 0xFF, 0x55, 0x00, 0x00, 0x03, 0x0A, 0x08, (byte) 0xBB, 0x00, 0x12, 0x00, 0x01, 0x02, 0x15, 0x7E, 0x76, 0x2C };
    }

    public boolean GetResult(){
        return isSuccess;
    }

    @Override
    public byte[] buildRequestCommand()
    {
        return command;
    }

    @Override
    public void receiveMsg(byte[] data, int len)
    {
        super.receiveMsg(data, len);
        if (data.length > 13 && data[12] == 0) {
            isSuccess = true;
        }
        else {
            isSuccess = false;
        }
    }

    public static boolean isProtocalRight(byte[] data, int len){
        if (len > 9 && (data[6] + 9) == len && data[9] == (byte)0x12)
            return true;

        return false;
    }
}

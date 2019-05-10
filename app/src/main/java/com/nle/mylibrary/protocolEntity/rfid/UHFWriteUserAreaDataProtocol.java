package com.nle.mylibrary.protocolEntity.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;

public class UHFWriteUserAreaDataProtocol extends DataProtocol
{
    private byte[] command;
    private boolean result;

    public UHFWriteUserAreaDataProtocol(){
    }

    public UHFWriteUserAreaDataProtocol(byte[] epc, int epclen, byte[] data, int datalen, byte dataaddr){
        if (epc.length < epclen || data.length < datalen)
            return;

        int msgLen = epclen + datalen + 15;

        command = new byte[msgLen];
        command[0] = (byte) (msgLen-1);
        command[1] = 0x00;
        command[2] = 0x03;
        command[3] = (byte) (datalen / 2);
        command[4] = (byte) (epclen / 2);
        int index = 5;
        System.arraycopy(epc ,0 ,command ,index, epclen);
        index += epclen;
        command[index] = 0x03;
        index++;
        command[index] = dataaddr;
        index++;
        System.arraycopy(data ,0 ,command ,index, datalen);
        command[msgLen - 3]=0x0C;

        byte[] crcdata = new byte[msgLen - 2];
        System.arraycopy(command ,0 ,crcdata ,0, msgLen - 2);

        byte[] crc = UHFReaderCrcCal.crccal2ByteArray(crcdata, (byte) crcdata.length);
        command[msgLen - 2]=crc[0];
        command[msgLen - 1]=crc[1];
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

        if (data[3] == 0x00) {
            result = true;
        }
        else {
            result = false;
        }
    }

    public boolean IsUHFWriteSuccess(){
        return result;
    }

    public byte[] getCommand()
    {
        return command;
    }

    public static boolean isProtocalRight(byte[] data, int len){
        if (len > 2 && (data[0] + 1) == len && data[2] == (byte)0x03)
            return true;

        return false;
    }
}

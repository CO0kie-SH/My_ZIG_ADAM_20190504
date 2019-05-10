package com.nle.mylibrary.protocolEntity.rfid;

import android.util.Log;
import com.nle.mylibrary.protocolEntity.DataProtocol;

public class UHFReadUserAreaDataProtocol extends DataProtocol
{
    private byte[] command;
    private byte[] userData;
    private boolean isSuccessed;

    public UHFReadUserAreaDataProtocol(){

    }

    public UHFReadUserAreaDataProtocol(byte[] epc, int epclen, byte dataaddr, byte len){
        if (epc.length < epclen)
            return;

        int msgLen = epclen + 15;

        command = new byte[msgLen];
        command[0] = (byte) (msgLen-1);
        command[1] = 0x00;
        command[2] = 0x02;
        command[3] = (byte) (epclen / 2);
        int index = 4;
        System.arraycopy(epc ,0 ,command ,index, epclen);
        index += epclen;
        command[index] = 0x03;
        index++;
        command[index] = dataaddr;
        index++;
        command[index] = (byte) (len / 2);
        index++;

        command[msgLen - 4]=0x00;
        command[msgLen - 3]=0x0C;
        byte[] crcdata = new byte[msgLen - 2];
        System.arraycopy(command ,0 ,crcdata ,0, msgLen - 2);
        byte[] crc = UHFReaderCrcCal.crccal2ByteArray(crcdata, (byte) crcdata.length);
        command[msgLen - 2]=crc[0];
        command[msgLen - 1]=crc[1];
        printData(command, command.length);
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
        userData = new byte[len - 6];
         if (data[3] == 0x00){
             System.arraycopy(data, 4, userData, 0, len - 6);
             isSuccessed = true;
         }
         else {
             isSuccessed = false;
         }
    }

    private void printData(byte[] receiveBytes, int len) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < len; i++) {
            byte itemByte = receiveBytes[i];
            String hexString = Integer.toHexString(itemByte & 0xff);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            stringBuffer.append(hexString).append(" ");
        }
        Log.e("receiveData", stringBuffer.toString());
    }

    public byte[] getUserData()
    {
        return userData;
    }

    public boolean isSuccessed()
    {
        return isSuccessed;
    }

    public byte[] getCommand()
    {
        return command;
    }

    public static boolean isProtocalRight(byte[] data, int len){
        if (len > 2 && (data[0] + 1) == len && data[2] == (byte)0x02)
            return true;

        return false;
    }
}

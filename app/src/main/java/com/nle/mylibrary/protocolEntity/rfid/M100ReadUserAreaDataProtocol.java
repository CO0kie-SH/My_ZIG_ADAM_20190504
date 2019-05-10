package com.nle.mylibrary.protocolEntity.rfid;

import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.util.Misc;

public class M100ReadUserAreaDataProtocol extends DataProtocol
{
    private byte[] command;
    private boolean isSuccess;
    private byte[] userData;

    public M100ReadUserAreaDataProtocol(){ }

    public M100ReadUserAreaDataProtocol(int dataaddr, int datalen){
        byte[] head = new byte[]{ (byte) 0xFF, 0x55, 0x00, 0x00, 0x03, 0x0A, 0x10 };
        byte[] body = new byte[16];
        command = new byte[25];
        byte[] part1 = new byte[]{ (byte) 0xBB, 0x00, 0x39, 0x00, 0x09, 0x00, 0x00, 0x00, 0x00, 0x03};
        System.arraycopy(part1, 0, body, 0, part1.length);
        int index = part1.length;
        byte[] sa = Misc.int2hex(dataaddr);
        System.arraycopy(sa, 0, body, index, 2);
        index += 2;
        byte[] dl = Misc.int2hex(datalen / 2);
        System.arraycopy(dl, 0, body, index, 2);
        index += 2;
        byte checksum = 0;
        for (int i = 1 ; i < body.length -2; i++) {
            checksum += body[i];
        }
        body[index] = checksum;
        index++;
        body[index] = 0x7E;
        System.arraycopy(head, 0, command, 0, head.length);
        System.arraycopy(body, 0, command, head.length, body.length);
        byte[] result = new byte[command.length - 4];
        System.arraycopy(command, 2, result, 0, result.length);
        byte[] crc = DataTools.calculateCrc16(result);
        command[command.length - 2] = crc[0];
        command[command.length - 1] = crc[1];
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
        byte[] let = new byte[]{data[10], data[11]};
        int pl = Misc.hex2int(let) - 15;
        byte[] epc = new byte[12];
        System.arraycopy(data, 15, epc, 0, 12);
        if (pl < 0 ) return;
        userData = new byte[pl];
        System.arraycopy(data, 27, userData, 0, pl);
        isSuccess = true;
    }

    public static boolean isProtocalRight(byte[] data, int len){
        if (len > 9 && (data[6] + 9) == len && data[9] == (byte)0x39)
            return true;

        return false;
    }

    public byte[] getUserData()
    {
        return userData;
    }

    public boolean isSuccessed(){
        return isSuccess;
    }

    public byte[] getCommand()
    {
        return command;
    }
}

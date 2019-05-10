package com.nle.mylibrary.protocolEntity.rfid;

import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;

public class M100SelectCardByEpcProtocol extends DataProtocol
{
    private byte[] command;
    private boolean isSuccess;

    public M100SelectCardByEpcProtocol(){
    }

    public M100SelectCardByEpcProtocol(byte[] epc){
        byte[] head = new byte[]{ (byte) 0xFF, 0x55, 0x00, 0x00, 0x03, 0x0A, 0x1A};
        byte[] body = new byte[26];
        command = new byte[35];
        byte[] part1 = new byte[]{ (byte) 0xBB, 0x00, 0x0C, 0x00, 0x13, 0x01, 0x00, 0x00, 0x00, 0x20, 0x60, 0x00};
        System.arraycopy(part1, 0, body, 0, part1.length);
        int index = part1.length;
        System.arraycopy(epc, 0, body, index, epc.length);
        index += 12;
        byte checksum = 0;
        for (int i = 1 ; i < body.length - 2; i++) {
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
        if (len > 9 && (data[6] + 9) == len && data[9] == (byte)0x0c)
            return true;

        return false;
    }

    public byte[] getCommand()
    {
        return command;
    }
}

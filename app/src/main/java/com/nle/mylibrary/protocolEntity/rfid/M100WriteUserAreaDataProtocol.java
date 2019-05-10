package com.nle.mylibrary.protocolEntity.rfid;

import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.util.Misc;

public class M100WriteUserAreaDataProtocol extends DataProtocol
{
    private byte[] command;
    private boolean isSuccess;

    public M100WriteUserAreaDataProtocol(){ }

    public M100WriteUserAreaDataProtocol(byte[] data, int datalen, int dataaddr){
        byte[] head = new byte[]{ (byte) 0xFF, 0x55, 0x00, 0x00, 0x03, 0x0A, (byte) (datalen + 16) };
        byte[] body = new byte[datalen + 16];
        command = new byte[head.length + datalen + 18];
        byte[] part1 = new byte[]{ (byte) 0xBB, 0x00, 0x49};
        System.arraycopy(part1, 0, body, 0, part1.length);
        int index = part1.length;
        byte[] pl = Misc.int2hex(datalen + 9);
        System.arraycopy(pl, 0, body, index, 2);
        index += 2;
        byte[] part2 = new byte[]{ 0x00, 0x00, 0x00, 0x00, 0x03};
        System.arraycopy(part2, 0, body, index, part2.length);
        index += part2.length;
        byte[] sa = Misc.int2hex(dataaddr);
        System.arraycopy(sa, 0, body, index, 2);
        index += 2;
        byte[] dl = Misc.int2hex(datalen / 2);
        System.arraycopy(dl, 0, body, index, 2);
        index += 2;
        System.arraycopy(data, 0, body, index, datalen);
        index += datalen;
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

    @Override
    public byte[] buildRequestCommand()
    {
        return command;
    }

    @Override
    public void receiveMsg(byte[] data, int len)
    {
        super.receiveMsg(data, len);
        if (data.length > 28 && data[27] == 0) {
            isSuccess = true;
        }
        else {
            isSuccess = false;
        }
    }

    public boolean isSuccess(){
        return isSuccess;
    }

    public byte[] getCommand()
    {
        return command;
    }

    public static boolean isProtocalRight(byte[] data, int len){
        if (len > 9 && (data[6] + 9) == len && data[9] == (byte)0x49)
            return true;

        return false;
    }
}

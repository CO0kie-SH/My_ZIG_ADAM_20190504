package com.nle.mylibrary.protocolEntity.rfid;

import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import java.util.ArrayList;
import java.util.List;

public class M100ScanEpcCodesProtocol extends DataProtocol
{
    private byte[] command;
    private List<byte[]> epcs = new ArrayList<>();
    private static byte[] dataAll4M100 ;
    private static int index4M100;

    public M100ScanEpcCodesProtocol(){
        command = new byte[]{ (byte) 0xFF, 0x55, 0x00 , 0x00 , 0x03 , 0x0A ,0x07 , (byte) 0xBB,0x00 ,0x22 ,0x00 ,0x00 ,0x22 ,0x7E, (byte) 0xC7, (byte) 0xC0 };
    }

    public boolean dataInputStream(byte[] data, int len) {
        if (data[6] + 9 == len) {
            if (data[9] == 0x22) {
                receiveMsg(data, len);
            }
        }
        else if(index4M100 != 0 || data[6] + 9 >= len){
            if (data[6] + 9 >= index4M100 + len ) {
                if (dataAll4M100 == null)
                    dataAll4M100 = new byte[data[6] + 9];
                if (index4M100 > dataAll4M100.length)
                    return false;
                System.arraycopy(data, 0, dataAll4M100, index4M100, (len > dataAll4M100.length - index4M100) ?dataAll4M100.length - index4M100:len);
                index4M100 += len;
            }
            else {
                if (index4M100 > dataAll4M100.length)
                    return false;
                System.arraycopy(data, 0, dataAll4M100, index4M100, (len > dataAll4M100.length - index4M100) ?dataAll4M100.length - index4M100:len);
                if (dataAll4M100.length > 9 && dataAll4M100[9] == 0x22) {
                    byte[] result = new byte[dataAll4M100.length - 4];
                    System.arraycopy(dataAll4M100, 2, result, 0, result.length);
                    byte[] crc = DataTools.calculateCrc16(result);
                    if (dataAll4M100[dataAll4M100.length - 2] == crc[0] && dataAll4M100[dataAll4M100.length - 1] == crc[1]) {
                        receiveMsg(dataAll4M100, dataAll4M100.length);
                    }
                    else {
                        dataAll4M100 = null;
                    }
                    index4M100 = 0;
                }
            }
        }

        return true;
    }

    public List<byte[]> getResult(){
        return epcs;
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
        if (len > 7) {
            int epcCount = data[6] / 24;

            if (epcCount > 0) {
                epcs = new ArrayList<>();
                for (int num = 0; num < epcCount; num++) {
                    byte[] preEpcData = new byte[12];
                    int index = getIndedWithEpcNum(num);
                    System.arraycopy(data, index + 8, preEpcData, 0, 12);
                    epcs.add(preEpcData);
                }
            }
        }
    }

    private int getIndedWithEpcNum(int num){
        return 24 * num + 7;
    }
}

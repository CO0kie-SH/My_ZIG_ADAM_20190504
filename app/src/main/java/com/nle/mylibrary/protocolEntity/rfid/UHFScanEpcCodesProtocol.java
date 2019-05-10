package com.nle.mylibrary.protocolEntity.rfid;

import android.util.Log;
import com.nle.mylibrary.protocolEntity.DataProtocol;

import java.util.ArrayList;
import java.util.List;

public class UHFScanEpcCodesProtocol extends DataProtocol
{
    private byte[] command;
    private List<byte[]> epcs = new ArrayList<>();
    private static byte[] dataAll ;
    private static int index ;

    public UHFScanEpcCodesProtocol(){
        command = new byte[]{4, 0, 1, (byte) 219, (byte) 75};
    }

    public boolean dataInputStream(byte[] data, int len) {
        if (data[0] + 1 == len) {
            if (data[2] == 0x01 && data[3] == 0x01) {
                receiveMsg(data, len);
            }
        } else if (index != 0 || data[0] + 1 >= len) {
            if (data[0] + 1 >= index + len) {
                if (dataAll == null)
                    dataAll = new byte[data[0] + 1];
                if (index > dataAll.length)
                    return false;
                System.arraycopy(data, 0, dataAll, index, (len > dataAll.length - index) ? dataAll.length - index : len);
                index += len;
            } else {
                System.arraycopy(data, 0, dataAll, index, (len > dataAll.length - index) ? dataAll.length - index : len);

                byte[] result = new byte[dataAll.length - 2];
                System.arraycopy(dataAll, 0, result, 0, result.length);
                byte[] crc = UHFReaderCrcCal.crccal2ByteArray(result, (byte) result.length);
                if (dataAll[dataAll.length - 2] == crc[0] && dataAll[dataAll.length - 1] == crc[1]) {
                    receiveMsg(dataAll, dataAll.length);
                } else {
                    dataAll = null;
                }
                index = 0;
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
        int epcNum = data[4];
        int index = 6;
        for (int i = 0; (i < epcNum) && (index < len );i++){
            byte[] epc;
            if (i  == 0) {
                int epcLen = data[5];
                epc = new byte[epcLen];
                System.arraycopy(data,6, epc, 0, epcLen);
                index += epcLen + 1;
            }
            else {
                int epcLen = data[index - 1];
                epc = new byte[epcLen];
                System.arraycopy(data,index, epc, 0, epcLen);
                index += epcLen + 1;
            }
            epcs.add(epc);
        }
        Log.e("","");
    }
}

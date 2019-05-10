package com.nle.mylibrary.processStrategy.zigbee;

import android.util.Log;

import com.nle.mylibrary.processStrategy.DataProcess;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.zigbee.ZigbeeSensorProtocolData;
import com.nle.mylibrary.util.DataTools;

import java.util.ArrayList;
import java.util.List;

public class ZBSensor implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        try {
            if (len > 0) {
                byte[] remainData = new byte[0];

                int startIndex = DataTools.indexOf(data, new byte[]{(byte) 0xfe}, 0);
                if (startIndex >= 0) {//先假设匹配上e9e3的是合法报文，解析出来正确的IDataProtocol对象
                    int dataLen = data[1 + startIndex] & 0xff;
                    byte[] realSnipData = new byte[4 + dataLen + 1];
                    System.arraycopy(data, startIndex, realSnipData, 0, realSnipData.length);
                    DataProtocol dataProtocol = analyzeSensorData(realSnipData, realSnipData.length);

                    if (dataProtocol != null) {//解析成功，截取剩下的byte[]数组
                        dataProtocols.add(dataProtocol);
                        remainData = new byte[len - realSnipData.length];
                        System.arraycopy(data, startIndex + realSnipData.length, remainData, 0, remainData.length);
                    } else {//解析失败，截取e9e3之后的byte数组
                        remainData = new byte[len - 2];
                        System.arraycopy(data, startIndex + 2, remainData, 0, remainData.length);
                    }
                }
                //迭代解析
                dataProtocols.addAll(process(remainData, remainData.length));
            }

        } catch (Exception ignore) {

        }
        return dataProtocols;

    }

    private static DataProtocol analyzeSensorData(byte[] receiveBytes, int len) {
        if (!checkCHKCode4SensorData(receiveBytes)) {
            return null;
        }
        DataProtocol dataProtocol = null;
        try {
            if ((receiveBytes[2] == 0x46) && (receiveBytes[3] == (byte) 0x87) && (receiveBytes[6] == 0x02) && (receiveBytes[7] == (byte) 0x00)) {
                dataProtocol = new ZigbeeSensorProtocolData();
                dataProtocol.receiveMsg(receiveBytes, len);
            }
        } catch (Exception ignore) {
            Log.e("DataToolsS", "data format error");
        }
        return dataProtocol;
    }

    private static boolean checkCHKCode4SensorData(byte[] receiveBytes) {
        int result = receiveBytes[1];
        for (int i = 2; i < receiveBytes.length - 1; i++) {
            result = result ^ receiveBytes[i];
        }
        return (byte) result == receiveBytes[receiveBytes.length - 1];
    }

}

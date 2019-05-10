package com.nle.mylibrary.processStrategy.zigbee;

import android.util.Log;

import com.nle.mylibrary.processStrategy.DataProcess;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.zigbee.ConnectDetectProtocolData;
import com.nle.mylibrary.protocolEntity.zigbee.DeviceInfoProtocol;
import com.nle.mylibrary.protocolEntity.zigbee.DeviceSettingProtocol;
import com.nle.mylibrary.util.DataTools;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfo implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        try {
            if (len > 0) {
                byte[] remainData = new byte[0];

                int startIndex = DataTools.indexOf(data, new byte[]{(byte) 0xe9, (byte) 0xe3}, 0);
                if (startIndex >= 0) {//先假设匹配上e9e3的是合法报文，解析出来正确的IDataProtocol对象
                    int dataLen = data[2 + startIndex] & 0xff;
                    byte[] realSnipData = new byte[2 + dataLen + 2];
                    System.arraycopy(data, startIndex, realSnipData, 0, realSnipData.length);
                    DataProtocol dataProtocol = analyzeProtocolType(realSnipData, realSnipData.length);

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

    private static DataProtocol analyzeProtocolType(byte[] receiveBytes, int len) {
        if (!checkCHKCode(receiveBytes)) {
            return null;
        }
        DataProtocol dataProtocol = null;
        try {
            if (len == 9) {
                dataProtocol = new ConnectDetectProtocolData();
            } else if ((len == 11 || len == 8)) {
                DeviceInfoProtocol deviceInfoProtocol = new DeviceInfoProtocol();
                deviceInfoProtocol.setSuccess(receiveBytes[3] == 0x00);
                dataProtocol = deviceInfoProtocol;
            } else if (len == 5) {
                DeviceSettingProtocol deviceSettingProtocol = new DeviceSettingProtocol();
                deviceSettingProtocol.setSettingSuccess(receiveBytes[3] == 0);
                dataProtocol = deviceSettingProtocol;
            }
            if (dataProtocol != null) {
                dataProtocol.receiveMsg(receiveBytes, len);
            }
        } catch (Exception ignore) {
            Log.e("DataToolsS", "data format error");
        }
        return dataProtocol;
    }

    private static boolean checkCHKCode(byte[] receiveBytes) {
        int result = receiveBytes[2];
        for (int i = 3; i < receiveBytes.length - 1; i++) {
            result = result ^ receiveBytes[i];
        }
        return (byte) result == receiveBytes[receiveBytes.length - 1];
    }
}

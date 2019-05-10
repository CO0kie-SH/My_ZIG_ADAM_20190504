package com.nle.mylibrary.processStrategy.zigbee;

import com.nle.mylibrary.processStrategy.DataProcess;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.zigbee.ZigBeeRelayProtocol;
import com.nle.mylibrary.util.DataTools;

import java.util.ArrayList;
import java.util.List;

public class ZBRelay implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        try {
            if (len > 0) {
                byte[] remainData = new byte[0];

                int startIndex = DataTools.indexOf(data, new byte[]{(byte) 0xff, (byte) 0xf5}, 0);
                if (startIndex >= 0) {//先假设匹配上e9e3的是合法报文，解析出来正确的IDataProtocol对象
                    int dataLen = data[2 + startIndex] & 0xff;
                    byte[] realSnipData = new byte[3 + dataLen + 1];
                    System.arraycopy(data, startIndex, realSnipData, 0, realSnipData.length);
                    DataProtocol dataProtocol = analyzeRelay(realSnipData, realSnipData.length);

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

    private static DataProtocol analyzeRelay(byte[] receiveBytes, int len) {
        if (!checkCHKCode4RelayData(receiveBytes)) {
            return null;
        }
        ZigBeeRelayProtocol relayProtocol = new ZigBeeRelayProtocol();
        relayProtocol.receiveMsg(receiveBytes, len);
        return relayProtocol;
    }

    private static boolean checkCHKCode4RelayData(byte[] bytes) {
        byte result = bytes[0];
        for (int i = 1; i < bytes.length - 1; i++) {
            result = (byte) (result + bytes[i]);
        }
        result = (byte) (~result + 1);
        return result == bytes[bytes.length - 1];
    }
}

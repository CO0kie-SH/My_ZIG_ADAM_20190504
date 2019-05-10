package com.nle.mylibrary.processStrategy.modbus4150;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.modbus.ModbusSensorProtocol4150;
import com.nle.mylibrary.processStrategy.DataProcess;
import com.nle.mylibrary.util.DataTools;

import java.util.ArrayList;
import java.util.List;

public class MD4150Sensor implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        DataProtocol dataProtocol = null;
        if (len == 6 && data[1] == 0x01 && data[2] == 0x01) {
            byte[] preByte = new byte[4];
            System.arraycopy(data, 0, preByte, 0, preByte.length);
            byte crcH = DataTools.calculateCrc16(preByte)[0];
            byte crcL = DataTools.calculateCrc16(preByte)[1];
            if (data[4] == crcL && data[5] == crcH) {
                dataProtocol = new ModbusSensorProtocol4150();
                dataProtocol.receiveMsg(data, len);
            }
        }
        dataProtocols.add(dataProtocol);
        return dataProtocols;
    }
}

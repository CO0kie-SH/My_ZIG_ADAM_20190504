package com.nle.mylibrary.processStrategy.modbus4017;

import com.nle.mylibrary.processStrategy.DataProcess;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.modbus.ModbusProtocol4017;
import com.nle.mylibrary.util.DataTools;

import java.util.ArrayList;
import java.util.List;

public class MD4017Sensor implements DataProcess {
    private byte[] dataStorage = new byte[0];

    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        DataProtocol dataProtocol = null;
        //合法的4017数据长度
        int wholeDataLen = 21;
        if (len == wholeDataLen && data[1] == 0x03 && data[2] == 0x10) {
            dataProtocol = parseProtocol(data, len);
        } else {
            int preDataStorageLen = dataStorage.length;
            byte[] preDataStorage = new byte[preDataStorageLen];
            System.arraycopy(dataStorage, 0, preDataStorage, 0, preDataStorageLen);

            dataStorage = new byte[preDataStorageLen + len];
            System.arraycopy(preDataStorage, 0, dataStorage, 0, preDataStorageLen);
            System.arraycopy(data, 0, dataStorage, preDataStorageLen, len);
            if (dataStorage.length >= wholeDataLen) {
                dataProtocol = parseProtocol(dataStorage, wholeDataLen);
                byte[] newDataStorage = new byte[dataStorage.length - wholeDataLen];
                System.arraycopy(dataStorage, wholeDataLen, newDataStorage, 0, newDataStorage.length);//截取前面完整的协议数据，留下剩下的
            }

        }
        if (dataProtocol != null) {
            dataProtocols.add(dataProtocol);
        }

        return dataProtocols;
    }

    private DataProtocol parseProtocol(byte[] data, int len) {
        DataProtocol dataProtocol = null;
        byte[] preByte = new byte[19];
        System.arraycopy(data, 0, preByte, 0, preByte.length);
        byte crcH = DataTools.calculateCrc16(preByte)[0];
        byte crcL = DataTools.calculateCrc16(preByte)[1];
        if (data[19] == crcL && data[20] == crcH) {
            dataProtocol = new ModbusProtocol4017();
            dataProtocol.receiveMsg(data, len);
        }
        return dataProtocol;
    }
}

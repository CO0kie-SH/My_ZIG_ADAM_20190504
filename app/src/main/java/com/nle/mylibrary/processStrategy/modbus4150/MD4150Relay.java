package com.nle.mylibrary.processStrategy.modbus4150;

import com.nle.mylibrary.processStrategy.DataProcess;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.util.DataTools;

import java.util.ArrayList;
import java.util.List;

public class MD4150Relay implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] receiveBytes, int receiveLen) {
        return null;
    }

    public DataProtocol parseModbus4150Relay(List<DataProtocol> iDataProtocols, byte[] data, int len) {
        List<DataProtocol> copyDataProtocols = new ArrayList<>(iDataProtocols);
        DataProtocol matchDataProtocol = null;
        for (DataProtocol iDataProtocol : copyDataProtocols) {
            if (DataTools.bytesEqual(iDataProtocol, data, len)) {
                matchDataProtocol = iDataProtocol;
                matchDataProtocol.receiveMsg(data, len);
            }
        }
        iDataProtocols.remove(matchDataProtocol);
        return matchDataProtocol;
    }
}

package com.nle.mylibrary.processStrategy.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.M100SelectModeProtocol;
import com.nle.mylibrary.processStrategy.DataProcess;

import java.util.ArrayList;
import java.util.List;

public class M100SetSelectMode implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        DataProtocol dataProtocol = null;
        if (M100SelectModeProtocol.isProtocalRight(data, len)) {
            dataProtocol = new M100SelectModeProtocol();
            dataProtocol.receiveMsg(data, len);
        }
        dataProtocols.add(dataProtocol);
        return dataProtocols;
    }
}

package com.nle.mylibrary.processStrategy.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.M100WriteUserAreaDataProtocol;
import com.nle.mylibrary.processStrategy.DataProcess;

import java.util.ArrayList;
import java.util.List;

public class M100WriteUserArea implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        DataProtocol dataProtocol = null;
        if (M100WriteUserAreaDataProtocol.isProtocalRight(data, len)) {
            dataProtocol = new M100WriteUserAreaDataProtocol();
            dataProtocol.receiveMsg(data, len);
        }
        dataProtocols.add(dataProtocol);
        return dataProtocols;
    }
}

package com.nle.mylibrary.processStrategy.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.UHFReadUserAreaDataProtocol;
import com.nle.mylibrary.processStrategy.DataProcess;

import java.util.ArrayList;
import java.util.List;

public class UHFReadUserArea implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        DataProtocol dataProtocol = null;
        if (UHFReadUserAreaDataProtocol.isProtocalRight(data, len)) {
            dataProtocol = new UHFReadUserAreaDataProtocol();
            dataProtocol.receiveMsg(data, len);
        }
        dataProtocols.add(dataProtocol);
        return dataProtocols;
    }
}

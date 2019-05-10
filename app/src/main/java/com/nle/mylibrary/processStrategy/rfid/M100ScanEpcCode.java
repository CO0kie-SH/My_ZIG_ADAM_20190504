package com.nle.mylibrary.processStrategy.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.M100ScanEpcCodesProtocol;
import com.nle.mylibrary.processStrategy.DataProcess;

import java.util.ArrayList;
import java.util.List;

public class M100ScanEpcCode implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        M100ScanEpcCodesProtocol dataProtocol = new M100ScanEpcCodesProtocol();
        dataProtocols.add(!dataProtocol.dataInputStream(data, len) ? null : dataProtocol);
        return dataProtocols;
    }
}

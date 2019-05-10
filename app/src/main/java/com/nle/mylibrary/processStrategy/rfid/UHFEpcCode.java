package com.nle.mylibrary.processStrategy.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.UHFScanEpcCodesProtocol;
import com.nle.mylibrary.processStrategy.DataProcess;

import java.util.ArrayList;
import java.util.List;

public class UHFEpcCode implements DataProcess {
    @Override
    public List<DataProtocol> process(byte[] data, int len) {
        List<DataProtocol> dataProtocols = new ArrayList<>();
        UHFScanEpcCodesProtocol dataProtocol = new UHFScanEpcCodesProtocol();
        dataProtocols.add(!dataProtocol.dataInputStream(data, len) ? null : dataProtocol);
        return dataProtocols;
    }
}

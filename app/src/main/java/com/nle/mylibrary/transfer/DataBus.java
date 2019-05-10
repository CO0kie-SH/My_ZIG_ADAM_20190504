package com.nle.mylibrary.transfer;

import com.nle.mylibrary.processStrategy.DataProcess;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.util.LimitList;

public interface DataBus {
    void setup();

    DataBus addProcessStrategy(DataProcess dataProcesses);

     boolean openSuccess();

    LimitList<DataProtocol> getDataRep();

    void sendProtocol(DataProtocol dataProtocol);

    void setCommonListener(CommonListener commonListener);

    void removeCommonListener(CommonListener commonListener);

    void removeAllListener();

    void sendProtocolAsRequestFormat(DataProtocol dataProtocol);

    void close();
}

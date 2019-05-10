package com.nle.mylibrary.forUse.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.M100WriteUserAreaDataProtocol;
import com.nle.mylibrary.transfer.CommonListener;
import com.nle.mylibrary.transfer.DataBus;

class RfidWirteCommonListenerImpl implements CommonListener {
    private RFIDWriteListener rfidWriteListener;
    private DataBus dataBus;

    RfidWirteCommonListenerImpl(RFIDWriteListener rfidWriteListener, DataBus dataBus) {
        this.rfidWriteListener = rfidWriteListener;
        this.dataBus=dataBus;
    }

    @Override
    public void onResponse(DataProtocol dataProtocol) {
        if (dataProtocol instanceof M100WriteUserAreaDataProtocol) {
            M100WriteUserAreaDataProtocol m100WriteUserAreaDataProtocol = (M100WriteUserAreaDataProtocol) dataProtocol;
            if (rfidWriteListener != null) {
                rfidWriteListener.onResult(m100WriteUserAreaDataProtocol.isSuccess());
            }
            dataBus.removeCommonListener(this);
        }
    }
}
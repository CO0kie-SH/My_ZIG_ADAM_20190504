package com.nle.mylibrary.forUse.zigbee;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.zigbee.ZigBeeRelayProtocol;
import com.nle.mylibrary.transfer.CommonListener;
import com.nle.mylibrary.transfer.DataBus;

class ZbRelayCommonListenerImpl implements CommonListener {
    private ZigbeeControlListener controlListener;
    private DataBus dataBus;

    public ZbRelayCommonListenerImpl(ZigbeeControlListener controlListener, DataBus dataBus) {
        this.controlListener = controlListener;
        this.dataBus = dataBus;
    }


    @Override
    public void onResponse(DataProtocol dataProtocol) {
        if (dataProtocol instanceof ZigBeeRelayProtocol) {
            if (controlListener != null) {
                controlListener.onCtrl(true);
                dataBus.removeCommonListener(this);
            }
        }
    }
}
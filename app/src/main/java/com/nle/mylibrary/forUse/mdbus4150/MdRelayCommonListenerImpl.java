package com.nle.mylibrary.forUse.mdbus4150;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.modbus.ModbusRelayProtocol4150;
import com.nle.mylibrary.transfer.CommonListener;
import com.nle.mylibrary.transfer.DataBus;

class MdRelayCommonListenerImpl implements CommonListener {
    private MdBus4150RelayListener controlListener;
    private ModbusRelayProtocol4150 modbusRelayProtocol4150;
    private DataBus dataBus;

    MdRelayCommonListenerImpl(MdBus4150RelayListener controlListener, ModbusRelayProtocol4150 modbusRelayProtocol4150, DataBus dataBus) {
        this.controlListener = controlListener;
        this.modbusRelayProtocol4150 = modbusRelayProtocol4150;
        this.dataBus = dataBus;
    }


    @Override
    public void onResponse(DataProtocol dataProtocol) {
        if (dataProtocol instanceof ModbusRelayProtocol4150) {
            boolean isSuccess = byteEqual(dataProtocol.getReceiveData(), modbusRelayProtocol4150.buildRequestCommand());
            if (isSuccess) {
                if (controlListener != null) {
                    controlListener.onCtrl(true);
                }
                dataBus.removeCommonListener(this);
            }
        }
    }

    private boolean byteEqual(byte[] bytes0, byte[] bytes1) {
        if (bytes0 != null && bytes1 != null) {
            if (bytes0.length != bytes1.length) {
                return false;
            }
            for (int i = 0; i < bytes0.length; i++) {
                if (bytes0[i] != bytes1[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
package com.nle.mylibrary.forUse.mdbus4017;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.modbus.ModbusProtocol4017;
import com.nle.mylibrary.transfer.CommonListener;
import com.nle.mylibrary.transfer.DataBus;

public class MD4017CommonListenerImpl implements CommonListener {
    private MD4017ValListener md4017ValListener;
    private DataBus dataBus;

     MD4017CommonListenerImpl(MD4017ValListener md4017ValListener, DataBus dataBus) {
        this.md4017ValListener = md4017ValListener;
        this.dataBus = dataBus;
    }

    @Override
    public void onResponse(DataProtocol dataProtocol) {
        if (dataProtocol instanceof ModbusProtocol4017) {
            if (md4017ValListener != null) {
                ModbusProtocol4017 modbusProtocol4017 = (ModbusProtocol4017) dataProtocol;
                int[] vals = new int[8];
                vals[0] = modbusProtocol4017.getVin0();
                vals[1] = modbusProtocol4017.getVin1();
                vals[2] = modbusProtocol4017.getVin2();
                vals[3] = modbusProtocol4017.getVin3();
                vals[4] = modbusProtocol4017.getVin4();
                vals[5] = modbusProtocol4017.getVin5();
                vals[6] = modbusProtocol4017.getVin6();
                vals[7] = modbusProtocol4017.getVin7();
                md4017ValListener.onVal(vals);
                dataBus.removeCommonListener(this);
            }
        }else{
            System.out.println();
        }
    }
}

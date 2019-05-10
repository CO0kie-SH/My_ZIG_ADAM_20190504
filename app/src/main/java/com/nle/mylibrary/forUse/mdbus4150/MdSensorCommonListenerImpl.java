package com.nle.mylibrary.forUse.mdbus4150;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.modbus.ModbusSensorProtocol4150;
import com.nle.mylibrary.transfer.CommonListener;
import com.nle.mylibrary.transfer.DataBus;

class MdSensorCommonListenerImpl implements CommonListener {
    private int portBum;
    private MdBus4150SensorListener valListener;
    private DataBus dataBus;

    MdSensorCommonListenerImpl(int portBum, MdBus4150SensorListener valListener, DataBus dataBus) {
        this.portBum = portBum;
        this.valListener = valListener;
        this.dataBus = dataBus;
    }

    @Override
    public void onResponse(DataProtocol dataProtocol) {
        if (dataProtocol instanceof ModbusSensorProtocol4150) {
            ModbusSensorProtocol4150 modbusSensorProtocol41501 = (ModbusSensorProtocol4150) dataProtocol;
            if (valListener != null) {
                switch (portBum) {
                    case 0:
                        valListener.onVal(modbusSensorProtocol41501.DI0Val);
                        break;
                    case 1:
                        valListener.onVal(modbusSensorProtocol41501.DI1Val);
                        break;
                    case 2:
                        valListener.onVal(modbusSensorProtocol41501.DI2Val);
                        break;
                    case 3:
                        valListener.onVal(modbusSensorProtocol41501.DI3Val);
                        break;
                    case 4:
                        valListener.onVal(modbusSensorProtocol41501.DI4Val);
                        break;
                    case 5:
                        valListener.onVal(modbusSensorProtocol41501.DI5Val);
                        break;
                    case 6:
                        valListener.onVal(modbusSensorProtocol41501.DI6Val);
                        break;
                }

            }
            dataBus.removeCommonListener(this);
        }

    }
}
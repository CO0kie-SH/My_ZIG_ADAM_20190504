package com.nle.mylibrary.forUse.mdbus4017;

import com.nle.mylibrary.processStrategy.modbus4017.MD4017Sensor;
import com.nle.mylibrary.protocolEntity.modbus.ModbusProtocol4017;
import com.nle.mylibrary.forUse.BaseUniversalListener;
import com.nle.mylibrary.forUse.exeception.SerialException;
import com.nle.mylibrary.forUse.exeception.SocketException;
import com.nle.mylibrary.transfer.DataBus;
import com.nle.mylibrary.transfer.SocketDataBus;

public class MD4017 implements BaseUniversalListener {
    private DataBus dataBus;

    public MD4017(DataBus dataBus) {
        this.dataBus = dataBus;
        if (dataBus != null) {
            dataBus.addProcessStrategy(new MD4017Sensor());
            dataBus.setup();
        }
    }

    public void getVin(byte address, MD4017ValListener md4017ValListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ModbusProtocol4017 modbusProtocol4017 = new ModbusProtocol4017(address);
            dataBus.setCommonListener(new MD4017CommonListenerImpl(md4017ValListener, dataBus));
            dataBus.sendProtocol(modbusProtocol4017);
        }
    }
    public void getVin( MD4017ValListener md4017ValListener) throws Exception {
        getVin((byte) 2, md4017ValListener);
    }
    @Override
    public void stopConnect() {
        if (dataBus != null) {
            dataBus.close();
        }
    }
}

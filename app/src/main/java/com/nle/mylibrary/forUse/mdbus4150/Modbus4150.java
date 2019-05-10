package com.nle.mylibrary.forUse.mdbus4150;

import com.nle.mylibrary.processStrategy.modbus4150.MD4150Relay;
import com.nle.mylibrary.processStrategy.modbus4150.MD4150Sensor;
import com.nle.mylibrary.protocolEntity.modbus.ModbusRelayProtocol4150;
import com.nle.mylibrary.protocolEntity.modbus.ModbusSensorProtocol4150;
import com.nle.mylibrary.enums.modbus.Modbus4150DO;
import com.nle.mylibrary.forUse.BaseUniversalListener;
import com.nle.mylibrary.forUse.exeception.SerialException;
import com.nle.mylibrary.forUse.exeception.SocketException;
import com.nle.mylibrary.transfer.CommonListener;
import com.nle.mylibrary.transfer.DataBus;
import com.nle.mylibrary.transfer.SocketDataBus;
public class Modbus4150 implements BaseUniversalListener {
    private DataBus dataBus;


    public Modbus4150(DataBus dataBus) {
        this.dataBus = dataBus;
        if (dataBus != null) {
            dataBus.addProcessStrategy(new MD4150Relay()).addProcessStrategy(new MD4150Sensor());
            dataBus.setup();
        }

    }

    /**
     * 开继电器
     * @param portNum 对应设备端子
     * @param controlListener 回调监听
     * @throws Exception
     */
    public void openRelay(int portNum, MdBus4150RelayListener controlListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }
        Modbus4150DO modbus4150DO = Modbus4150DO.values()[portNum];
        ModbusRelayProtocol4150 modbusRelayProtocol4150 = new ModbusRelayProtocol4150(1, true, modbus4150DO);
        CommonListener commonListener = new MdRelayCommonListenerImpl(controlListener, modbusRelayProtocol4150, dataBus);
        dataBus.setCommonListener(commonListener);
        dataBus.sendProtocolAsRequestFormat(modbusRelayProtocol4150);
    }

    /**
     * 关继电器
     * @param portNum 对应设备端子
     * @param controlListener 回调监听
     * @throws Exception
     */
    public void closeRelay(int portNum, MdBus4150RelayListener controlListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }
        Modbus4150DO modbus4150DO = Modbus4150DO.values()[portNum];
        ModbusRelayProtocol4150 modbusRelayProtocol4150 = new ModbusRelayProtocol4150(1, false, modbus4150DO);
        CommonListener commonListener = new MdRelayCommonListenerImpl(controlListener, modbusRelayProtocol4150, dataBus);
        dataBus.setCommonListener(commonListener);
        dataBus.sendProtocolAsRequestFormat(modbusRelayProtocol4150);
    }

    /**
     * 获取设备传感器值
     * @param portNum 对应设备端子
     * @param valListener 回调监听
     * @throws Exception
     */
    public void getVal(int portNum, MdBus4150SensorListener valListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }
        CommonListener mySensorCommonListener = new MdSensorCommonListenerImpl(portNum, valListener, dataBus);
        dataBus.setCommonListener(mySensorCommonListener);
        dataBus.sendProtocol(new ModbusSensorProtocol4150((byte) 1));
    }

    @Override
    public void stopConnect() {
        dataBus.close();
    }

}

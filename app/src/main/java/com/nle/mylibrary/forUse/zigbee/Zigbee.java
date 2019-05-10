package com.nle.mylibrary.forUse.zigbee;

import com.nle.mylibrary.enums.zigbee.ZigbeeSensorType;
import com.nle.mylibrary.forUse.BaseUniversalListener;
import com.nle.mylibrary.forUse.exeception.SerialException;
import com.nle.mylibrary.forUse.exeception.SocketException;
import com.nle.mylibrary.processStrategy.zigbee.DeviceInfo;
import com.nle.mylibrary.processStrategy.zigbee.ZBRelay;
import com.nle.mylibrary.processStrategy.zigbee.ZBSensor;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.zigbee.ZigBeeRelayProtocol;
import com.nle.mylibrary.protocolEntity.zigbee.ZigbeeSensorProtocolData;
import com.nle.mylibrary.transfer.DataBus;
import com.nle.mylibrary.transfer.SocketDataBus;

import java.util.List;

public class Zigbee implements BaseUniversalListener {
    private DataBus dataBus;

    public Zigbee(DataBus dataBus) {
        this.dataBus = dataBus;
        if (dataBus != null) {
            dataBus.addProcessStrategy(new DeviceInfo()).addProcessStrategy(new ZBRelay()).addProcessStrategy(new ZBSensor());
            dataBus.setup();
        }
    }

    /**
     * 控制单联继电器
     * @param serialNum 系列号
     * @param isOpen 开 :true ; 关：false
     * @param controlListener 回调监听
     * @throws Exception SocketException或SerialException
     */
    public void ctrlSingleRelay(int serialNum, boolean isOpen, ZigbeeControlListener controlListener) throws Exception {
        ctrlRelay(serialNum, isOpen ? 1 : 0, controlListener);
    }

    /**
     * 控制双联继电器
     * @param serialNum 系列号
     * @param unite 第几联
     * @param isOpen  开 :true ; 关：false
     * @param controlListener 回调监听
     * @throws Exception  @throws Exception SocketException或SerialException
     */
    public void ctrlDoubleRelay(int serialNum, int unite, boolean isOpen, ZigbeeControlListener controlListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }
        if (unite == 1) {
            ctrlRelay(serialNum, isOpen ? 1 : 2, controlListener);
        } else if (unite == 2) {
            ctrlRelay(serialNum, isOpen ? 16 : 32, controlListener);
        }
    }

    private void ctrlRelay(int serialNum, int val, ZigbeeControlListener controlListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            byte serialL = (byte) (serialNum & 0x00ff);
            byte serialH = (byte) (serialNum >> 8);
            ZigBeeRelayProtocol zigBeeRelayProtocol = new ZigBeeRelayProtocol(serialL, serialH, (byte) val);
            dataBus.setCommonListener(new ZbRelayCommonListenerImpl(controlListener, dataBus));
            dataBus.sendProtocol(zigBeeRelayProtocol);
        }
    }

    /**
     *
     * @return 1表示有人，0表示无人
     */
    public double getPerson() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.PERSON);
            if (dataProtocol != null) {
                return dataProtocol.getVal0();
            }
        }
        return -1;
    }

    /**
     * 获取温湿度传感器值
     * 第一个值表示温度，第二个值表示湿度
     *
     */
    public double[] getTmpHum() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.TEM_HUM);
            if (dataProtocol != null) {
                return new double[]{dataProtocol.getVal0(), dataProtocol.getVal1()};
            }
        }
        return null;
    }

    /**
     * 获取光照传感器值
     * @return
     * @throws Exception
     */
    public double getLight() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.LIGHT);
            if (dataProtocol != null) {
                return dataProtocol.getVal0();
            }
        }
        return -1;
    }

    /**
     * 获取一氧化碳值
     * @return
     * @throws Exception
     */
    public double getCO() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.CO);
            if (dataProtocol != null) {
                return dataProtocol.getVal0();
            }
        }
        return -1;
    }

    /**
     * 获取可燃气体值
     * @return
     * @throws Exception
     */
    public double getGas() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.COM_GAS);
            if (dataProtocol != null) {
                return dataProtocol.getVal0();
            }
        }
        return -1;
    }

    /**
     * 获取火焰传感器值
     * 1表示有，0表示无
     *
     * @throws SocketException
     */
    public double getFire() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.FIRE);
            if (dataProtocol != null) {
                return dataProtocol.getVal0();
            }
        }
        return -1;
    }

    /**
     * 获取酒精传感器值
     * @return
     * @throws Exception
     */
    public double getAlcohol() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.ALCOHOL);
            if (dataProtocol != null) {
                return dataProtocol.getVal0();
            }
        }
        return -1;
    }

    /**
     * 获取重力传感器值
     * @return 数组分别表示X,Y,Z方向的值
     * @throws Exception
     */
    public double[] getWeight() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.WEIGHT);
            if (dataProtocol != null) {
                return new double[]{dataProtocol.getVal0(), dataProtocol.getVal1(), dataProtocol.getVal2()};
            }
        }
        return null;
    }

    /**
     * 获取陀螺仪传感器值
     * @return 数组分别表示X,Y,Z方向的值
     * @throws Exception
     */
    public double[] getGyro() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.GYRO);
            if (dataProtocol != null) {
                return new double[]{dataProtocol.getVal0(), dataProtocol.getVal1(), dataProtocol.getVal2()};
            }
        }
        return null;
    }

    /**
     * 获取四通道传感器值
     * @return 数组分别表示通道1到4的传感值
     * @throws Exception
     */
    public double[] getFourEnter() throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        } else {
            ZigbeeSensorProtocolData dataProtocol = getTargetDataProtocol(ZigbeeSensorType.FOUR_ENTER);
            if (dataProtocol != null) {
                return new double[]{dataProtocol.getVal0(), dataProtocol.getVal1(), dataProtocol.getVal2(), dataProtocol.getVal3()};
            }
        }
        return null;
    }


    private ZigbeeSensorProtocolData getTargetDataProtocol(ZigbeeSensorType sensorType) {
        List<DataProtocol> dataProtocols = dataBus.getDataRep().get();
        for (int i = dataProtocols.size() - 1; i >= 0; i--) {
            DataProtocol dataProtocol = dataProtocols.get(i);
            if (dataProtocol instanceof ZigbeeSensorProtocolData) {
                ZigbeeSensorProtocolData zigbeeSensorProtocolData = (ZigbeeSensorProtocolData) dataProtocol;
                ZigbeeSensorType zigbeeSensorType = zigbeeSensorProtocolData.getSensorType();
                if (zigbeeSensorType == sensorType) {
                    return zigbeeSensorProtocolData;
                }
            }
        }
        return null;
    }


    @Override
    public void stopConnect() {
        if (dataBus != null) {
            dataBus.close();
        }
    }

}

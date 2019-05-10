package com.nle.mylibrary.protocolEntity.zigbee;


import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.enums.zigbee.ZigbeeSensorType;

/**
 * 传感器数据协议解析
 */
public class ZigbeeSensorProtocolData extends DataProtocol {
    private ZigbeeSensorType sensorType;
    private double val0;
    private double val1;
    private double val2;
    private double val3;
    private double val4;//预留
    private double val5;//预留
    private long generalTime;
    private int sensorId;

    public ZigbeeSensorProtocolData() {
        generalTime = System.currentTimeMillis();
    }

    public long getGeneralTime() {
        return generalTime;
    }

    public ZigbeeSensorType getSensorType() {

        return sensorType;
    }


    public double getVal0() {
        return val0;
    }

    public double getVal1() {
        return val1;
    }

    public double getVal2() {
        return val2;
    }

    public double getVal3() {
        return val3;
    }

    public double getVal4() {
        return val4;
    }

    public double getVal5() {
        return val5;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getSensorId() {

        return sensorId;
    }

    @Override
    public byte[] buildRequestCommand() {
        return null;
    }

    @Override
    public void receiveMsg(byte[] data, int len) {
        this.sensorId = DataTools.parseUnSignData(data[16], data[15]);
        this.sensorType = ZigbeeSensorType.getSensorTypeByCode(data[17]);
        if (sensorType != null) {
            switch (sensorType) {
                case PERSON:
                    val0 = data[18] & 0xff;
                    break;
                case LIGHT:
                case CO:
                case COM_GAS:
                case FIRE:
                case ALCOHOL:
                    val0 = DataTools.parseUnSignData(data[19], data[18]) * 0.01d;
                    break;
                case TEM_HUM:
                    val0 = DataTools.parseSignData(data[19], data[18]) * 0.1d;
                    val1 = DataTools.parseSignData(data[21], data[20]) * 0.1d;
                    break;
                case WEIGHT:
                    val0 = DataTools.parseSignData(data[19], data[18]) * 2.d / 512;
                    val1 = DataTools.parseSignData(data[21], data[20]) * 2.d / 512;
                    val2 = DataTools.parseSignData(data[23], data[22]) * 2.d / 512;
                    break;
                case GYRO:
                    val0 = DataTools.parseSignData(data[19], data[18]);
                    val1 = DataTools.parseSignData(data[21], data[20]);
                    val2 = DataTools.parseSignData(data[23], data[22]);
                    break;
                case FOUR_ENTER:
                    val0 = DataTools.parseUnSignData(data[19], data[18]) * 3300d / 1023 / 150;
                    val1 = DataTools.parseUnSignData(data[21], data[20]) * 3300d / 1023 / 150;
                    val2 = DataTools.parseUnSignData(data[23], data[22]) * 3300d / 1023 / 150;
                    val3 = DataTools.parseUnSignData(data[25], data[24]) * 3300d / 1023 / 150;
                    break;
            }

        }


    }

}

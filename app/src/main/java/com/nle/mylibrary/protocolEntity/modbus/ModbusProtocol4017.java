package com.nle.mylibrary.protocolEntity.modbus;


import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;

public class ModbusProtocol4017 extends DataProtocol {
    private byte address = 2;

    private int vin0;
    private int vin1;
    private int vin2;
    private int vin3;
    private int vin4;
    private int vin5;
    private int vin6;
    private int vin7;

    public ModbusProtocol4017() {

    }

    public ModbusProtocol4017(byte address) {
        this.address = address;
    }

    @Override
    public byte[] buildRequestCommand() {
        byte[] command = new byte[8];
        byte[] dataBytes = new byte[6];
        dataBytes[0] = address;
        dataBytes[1] = 0x03;
        dataBytes[2] = 0x00;
        dataBytes[3] = 0x00;
        dataBytes[4] = 0x00;
        dataBytes[5] = 0x08;
        byte[] crc16 = DataTools.calculateCrc16(dataBytes);
        System.arraycopy(dataBytes, 0, command, 0, dataBytes.length);
        command[6] = crc16[1];
        command[7] = crc16[0];
        return command;
    }

    public int getVin0() {
        return vin0;
    }

    public int getVin1() {
        return vin1;
    }

    public int getVin2() {
        return vin2;
    }

    public int getVin3() {
        return vin3;
    }

    public int getVin4() {
        return vin4;
    }

    public int getVin5() {
        return vin5;
    }

    public int getVin6() {
        return vin6;
    }

    public int getVin7() {
        return vin7;
    }

    @Override
    public void receiveMsg(byte[] data, int len) {
        super.receiveMsg(data, len);
        this.vin0 = DataTools.parseUnSignData(data[3], data[4]);
        this.vin1 = DataTools.parseUnSignData(data[5], data[6]);
        this.vin2 = DataTools.parseUnSignData(data[7], data[8]);
        this.vin3 = DataTools.parseUnSignData(data[9], data[10]);
        this.vin4 = DataTools.parseUnSignData(data[11], data[12]);
        this.vin5 = DataTools.parseUnSignData(data[13], data[14]);
        this.vin6 = DataTools.parseUnSignData(data[15], data[16]);
        this.vin7 = DataTools.parseUnSignData(data[17], data[18]);
    }

}

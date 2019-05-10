package com.nle.mylibrary.protocolEntity.modbus;


import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;

public class ModbusSensorProtocol4150 extends DataProtocol {
    private byte address;
    public byte DI0Val;
    public byte DI1Val;
    public byte DI2Val;
    public byte DI3Val;
    public byte DI4Val;
    public byte DI5Val;
    public byte DI6Val;

    @Override
    public byte[] buildRequestCommand() {
        byte[] command = new byte[8];
        byte[] dataBytes = new byte[6];
        dataBytes[0] = address;
        dataBytes[1] = 0x01;
        dataBytes[2] = 0x00;
        dataBytes[3] = 0x00;
        dataBytes[4] = 0x00;
        dataBytes[5] = 0x07;
        byte[] crc16 = DataTools.calculateCrc16(dataBytes);
        System.arraycopy(dataBytes, 0, command, 0, dataBytes.length);
        command[6] = crc16[1];
        command[7] = crc16[0];
        return command;

    }

    public ModbusSensorProtocol4150() {
    }

    public ModbusSensorProtocol4150(byte address) {
        this.address = address;
    }


    @Override
    public void receiveMsg(byte[] data, int len) {
        super.receiveMsg(data, len);
        byte dataByte = data[3];
        DI6Val = (byte) ((dataByte & 64) >> 6);
        DI5Val = (byte) ((dataByte & 32) >> 5);
        DI4Val = (byte) ((dataByte & 16) >> 4);
        DI3Val = (byte) ((dataByte & 8) >> 3);
        DI2Val = (byte) ((dataByte & 4) >> 2);
        DI1Val = (byte) ((dataByte & 2) >> 1);
        DI0Val = (byte) (dataByte & 1);
    }


}

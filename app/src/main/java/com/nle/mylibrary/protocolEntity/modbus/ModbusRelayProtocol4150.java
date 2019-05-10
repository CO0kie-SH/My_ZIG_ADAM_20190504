package com.nle.mylibrary.protocolEntity.modbus;


import com.nle.mylibrary.util.DataTools;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.enums.modbus.Modbus4150DO;

public class ModbusRelayProtocol4150 extends DataProtocol {
    private byte address;
    private byte closeOrOpen;
    private byte channelNum;

    public ModbusRelayProtocol4150() {

    }

    @Override
    public byte[] buildRequestCommand() {
        byte[] command = new byte[8];
        byte[] dataBytes = new byte[6];
        dataBytes[0] = address;
        dataBytes[1] = 0x05;
        dataBytes[2] = 0x00;
        dataBytes[3] = channelNum;
        dataBytes[4] = closeOrOpen;
        dataBytes[5] = 0x00;

        byte[] crc16 = DataTools.calculateCrc16(dataBytes);
        System.arraycopy(dataBytes, 0, command, 0, dataBytes.length);
        command[6] = crc16[1];
        command[7] = crc16[0];
        return command;
    }

    /**
     * @param address modbus地址
     * @param open    true 开，false关
     */
    public ModbusRelayProtocol4150(int address, boolean open, Modbus4150DO modbus4150DO) {
        this.address = (byte) address;
        this.closeOrOpen = (byte) (open ? 0xff : 0x00);
        channelNum = modbus4150DO.getAddress();
    }

    public byte getAddress() {
        return address;
    }

    public byte getCloseOrOpen() {
        return closeOrOpen;
    }

    public byte getChannelNum() {
        return channelNum;
    }



}

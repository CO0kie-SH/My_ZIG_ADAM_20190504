package com.nle.mylibrary.protocolEntity.rfid;

public class UHFReaderCrcCal
{
    static {
        System.loadLibrary("serial_port");
    }

    private static native int crccalJNI(byte[] pucY, byte ucX);

    public static byte[] crccal2ByteArray(byte[] pucY, byte ucX){
        byte[] result = new byte[2];
        int crc = UHFReaderCrcCal.crccalJNI(pucY, (byte) ucX);
        byte high = (byte) (crc & 0xff);
        byte low = (byte) ((crc & 0xff00) >> 8);
        result[0] = high;
        result[1] = low;
        return result;
    }
}

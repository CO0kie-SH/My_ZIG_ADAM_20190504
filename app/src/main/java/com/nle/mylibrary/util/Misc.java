package com.nle.mylibrary.util;

public class Misc
{
    public static byte[] int2hex(int data){
        data = ((data & 0xff00) >> 8) | ((data & 0x00ff) << 8);
        byte high = (byte) (data & 0xff);
        byte low = (byte) ((data & 0xff00) >> 8);
        return new byte[]{high, low};
    }

    public static int hex2int(byte[] data){
        byte high = data[0];
        byte low = data[1];
        return high * 0xff + low;
    }

}

package com.nle.mylibrary.util;

import android.text.TextUtils;

import com.nle.mylibrary.protocolEntity.DataProtocol;

/**
 * Date:2018/11/2
 * create by pengxl
 */
public class DataTools {
    /**
     * @param sourceData 原byte数组
     * @param targetData 目标byte数组
     * @param fromIndex  从原sourceData匹配开始位置
     * @return 匹配成功开始位置
     */
    public static int indexOf(byte[] sourceData, byte[] targetData, int fromIndex) {
        final int sourceLength = sourceData.length;
        final int targetLength = targetData.length;
        if (fromIndex >= sourceLength || fromIndex < 0 || targetLength == 0 || targetLength > sourceLength) {
            return -1;
        }
        byte first = targetData[0];
        int max = (sourceLength - targetLength);

        for (int i = fromIndex; i <= max; i++) {
            if (sourceData[i] != first) {
                while (++i <= max && sourceData[i] != first) ;
            }
            if (i <= max) {
                int j = i + 1;
                int end = j + targetLength - 1;
                for (int k = 1; j < end && sourceData[j] == targetData[k]; j++, k++)
                    ;
                if (j == end) {
                    return i;
                }
            }
        }
        return -1;
    }




    public static boolean bytesEqual(DataProtocol dataProtocol, byte[] data, int dataLen) {
        byte[] requestCommand = dataProtocol.buildRequestCommand();
        if (requestCommand.length != dataLen) {
            return false;
        }
        for (int i = 0; i < dataLen; i++) {
            if (requestCommand[i] != data[i]) {
                return false;
            }
        }
        return true;
    }
    /**
     * 双字节转有符号位整形
     *
     * @param high 高位
     * @param low  低位
     */
    public static int parseSignData(byte high, byte low) {
        return (short) ((high << 8) + (low & 0xff));
    }

    /**
     * 双字节转无符号位整形
     *
     * @param high 高位
     * @param low  低位
     */
    public static int parseUnSignData(byte high, byte low) {
        return ((high & 0xff) << 8) + (low & 0xff);
    }

    public static String parse2ByteHexString(String hexString, boolean hasPreFix) {
        int len = hexString.length();
        StringBuilder stringBuilder = new StringBuilder(hasPreFix ? "0x" : "");
        for (int i = 0; i < 4 - len; i++) {
            stringBuilder.append("0");
        }
        stringBuilder.append(hexString);
        return stringBuilder.toString();
    }


    /**
     * modbus crc16计算
     *
     * @param bytes byte[0]表高位，byte[1]表低位
     */
    public static byte[] calculateCrc16(byte[] bytes) {
        int crc = 0x0000ffff;
        for (int i = 0; i < bytes.length; i++) {
            crc ^= ((int) bytes[i] & 0x000000ff);
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x00000001) != 0) {
                    crc >>= 1;
                    crc ^= 0x0000a001;
                } else {
                    crc >>= 1;
                }
            }
        }
        //高低位互换，输出符合相关工具对Modbus CRC16的运算
        crc = ((crc & 0xff00) >> 8) | ((crc & 0x00ff) << 8);
        byte high = (byte) (crc & 0xff);
        byte low = (byte) ((crc & 0xff00) >> 8);
        return new byte[]{high, low};
    }

    /**
     * 一字节转16进制字符串,不足2位补0
     */
    public static String oneByte2HexStr(byte b) {
        String hexString = Integer.toHexString(b & 0xff);
        hexString = hexString.length() == 1 ? "0" + hexString : hexString;
        return hexString.toUpperCase();
    }

    /**
     * 字节数组转16进制数字串
     *
     * @param bytes
     * @return
     */
    public static String formatByteArray(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte aByte : bytes) {
            stringBuilder.append(oneByte2HexStr(aByte)).append(" ");
        }
        return stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "").toString().toUpperCase();
    }

    /**
     * 字符串转16进制2字节数组
     * @param formatData
     * @return
     * @throws Exception
     */
    public static byte[] Str2ByteArray(String formatData) throws Exception{
        formatData = formatData.replaceAll(" ", "");
        byte[] data = new byte[formatData.length() / 2];
        for (int i = 0; i < formatData.length(); i = i + 2) {
            String str = formatData.substring(i, i + 2);
            data[i / 2] = (byte) ((Integer.valueOf(str, 16)) & 0xff);
        }
        return data;
    }

    public static boolean validCommand(String string) {
        String nonSpaceString = string.replace(" ", "");
        return !TextUtils.isEmpty(nonSpaceString) && (nonSpaceString.length()) % 2 == 0 && isHexString(nonSpaceString);
    }

    public static boolean isHexString(String string) {
        String regex = "^[0-9A-Fa-f]+$";
        return string.matches(regex);
    }

}

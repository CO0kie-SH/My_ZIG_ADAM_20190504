package com.nle.mylibrary.forUse.mdbus4017;

public class MD4017ValConvert {
    /**
     * modbus4017数值转换公式
     * @param md4017VIN 参考Md4017VIN枚举类
     * @param val 通道实际电流值
     * @return
     */
    public static float getRealValByType(Md4017VIN md4017VIN, int val) {
        return ((md4017VIN.getMax() - md4017VIN.getMin()) * 1F / 65535) * val + md4017VIN.getMin();
    }

}

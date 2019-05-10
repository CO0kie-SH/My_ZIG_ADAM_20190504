package com.nle.mylibrary.forUse.zigbee;

/**
 * 四通道相关传感器设备数值转换类
 */
public class FourChannelValConvert {
    private static double fourChannelConvertValue(double vv, int maxRange, int mixRange) {
        if (vv <= 4) {
            vv = 4;
        }
        //double retValue = ((vv - 4) / 16 * maxRange) + mixRange;

        return ((vv - 4) / 16 * (maxRange - mixRange));

    }

    /**
     * 土壤温度
     *
     * @param value 四输入通道电流值转换
     */
    public static double getSoilTemperatuer(double value) {

        return fourChannelConvertValue(value, 70, 0);
        //return ADAMConvertValue(value, 70, -10);
    }

    /**
     * 土壤水分
     *
     * @param value 四输入通道电流值转换
     */
    public static double getSoilMoisture(double value) {
        return fourChannelConvertValue(value, 50, 0);
    }

    /**
     * 水温
     *
     * @param value 四输入通道电流值转换
     */
    public static double getWaterTemperature(double value) {
        return fourChannelConvertValue(value, 200, -50);
    }

    /**
     * 水位
     *
     * @param value 四输入通道电流值转换
     */
    public static double getWaterLevel(double value) {
        return fourChannelConvertValue(value, 1, 0);
    }

    /**
     * 光照
     *
     * @param value 四输入通道电流值转换
     */
    public static double getLight(double value) {
        return fourChannelConvertValue(value, 20000, 0);
    }

    /**
     * 温度
     *
     * @param value 四输入通道电流值转换
     */
    public static double getTemperature(double value) {
        return fourChannelConvertValue(value, 50, 0);
    }

    /**
     * 湿度
     *
     * @param value 四输入通道电流值转换
     */
    public static double getHumidity(double value) {
        return fourChannelConvertValue(value, 100, 0);
    }

    /**
     * 噪音
     *
     * @param value 四输入通道电流值转换
     */
    public static double getNoice(double value) {
        return fourChannelConvertValue(value, 500, 0);
    }

    /**
     * 二氧化碳
     *
     * @param value 四输入通道电流值转换
     */
    public static double getCO2(double value) {
        return fourChannelConvertValue(value, 5000, 0);
    }


}

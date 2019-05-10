package com.nle.mylibrary.transfer;

public class DataBusFactory {

    /**
     * 创建 基于串口直连的通讯连接，用完请即时关闭
     * @param serialIndex 串口号
     * @param baud 波特率
     * @return DataBus
     */
    public static DataBus newSerialDataBus(int serialIndex, int baud) {
        String serialName = "ttySAC" + serialIndex;
        return newSerialDataBus(serialName, baud);
    }

    /**
     * 创建 基于串口直连的通讯连接，用完请即时关闭
     * @param serialName 串口名称
     * @param baud 波特率
     * @return DataBus
     */
    public static DataBus newSerialDataBus(String serialName, int baud) {
        return newSerialDataBus(serialName, baud, 1, 8, 'N');
    }

    /**
     *  创建 基于串口直连的通讯连接，用完请即时关闭
     * @param serialName 串口名称
     * @param baud 波特率
     * @param stop 停止位
     * @param data 数据位
     * @param check 校验位
     * @return DataBus
     */
    public static DataBus newSerialDataBus(String serialName, int baud, int stop, int data, char check) {
        return new SerialDataBus(serialName, baud, stop, data, check);
    }

    /**
     * 创建基于socket的串口服务器通讯连接，用完请即时关闭
     * @param ip IP地址 串口服务器地址
     * @param port 端口号 对应端口的端口号
     * @return DataBus
     */
    public static DataBus newSocketDataBus(String ip, int port) {
        return new SocketDataBus(ip, port);
    }

}

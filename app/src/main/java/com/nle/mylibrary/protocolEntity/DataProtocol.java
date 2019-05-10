package com.nle.mylibrary.protocolEntity;

import java.io.Serializable;

/**
 * 表示串口通讯时，根据协议的分类区分传输的数据类型
 */
public abstract class DataProtocol implements Serializable {
    protected byte[] receiveData;

    public byte[] getReceiveData() {
        return receiveData;
    }

    public abstract byte[] buildRequestCommand();

    /**
     * 接收消息，实现类可以覆盖此方法，以实现返回的数据转换
     *
     * @param data 原始数据
     * @param len  数据长度
     */
    public void receiveMsg(byte[] data, int len) {
        if (data.length != len) {
            byte[] realData = new byte[len];
            System.arraycopy(data, 0, realData, 0, len);
            this.receiveData = realData;
        } else
            this.receiveData = data;
    }

}

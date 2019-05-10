package com.nle.mylibrary.processStrategy;

import com.nle.mylibrary.protocolEntity.DataProtocol;

import java.util.List;

/**
 * 协议解析策略接口
 */
public interface DataProcess {

    List<DataProtocol> process(byte[] data, int len);
}

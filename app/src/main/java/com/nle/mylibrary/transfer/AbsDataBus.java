package com.nle.mylibrary.transfer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.nle.mylibrary.processStrategy.DataProcess;
import com.nle.mylibrary.processStrategy.modbus4150.MD4150Relay;
import com.nle.mylibrary.processStrategy.zigbee.ZBSensor;
import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.util.LimitList;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsDataBus implements DataBus {
    private Handler handler;

    private List<DataProcess> dataProcesses = new ArrayList<>();//协议解析策略集合
    protected Boolean DEBUG = false;
    protected LimitList<DataProtocol> waitingCommand = new LimitList<>(512);//下达命令存储列表
    protected final LimitList<CommonListener> commonListeners = new LimitList<>(64);
    protected List<DataProtocol> waitResponseParsePro = new ArrayList<>();//响应集合，针对那些需要根据请求来判断是否合法的数据协议
    protected LimitList<DataProtocol> dataRep = new LimitList<>(128);//zigbee传感器数据使用列表缓存,对于主动上报的类型都应该如此，而非回调

    protected AbsDataBus() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public LimitList<DataProtocol> getDataRep() {
        return dataRep;
    }

    protected void conversionData(byte[] receiveBytes, int receiveLen) {
        if (DEBUG) {
            printLogData(receiveBytes, receiveLen, "receiveData");
        }
        List<DataProtocol> dataProtocols4CallBack = new ArrayList<>();

        for (DataProcess processStrategy : dataProcesses) {
            if (processStrategy instanceof MD4150Relay) {//modbus4150执行器是原报文返回，所以特殊处理
                MD4150Relay md4150Relay = (MD4150Relay) processStrategy;
                dataProtocols4CallBack.add(md4150Relay.parseModbus4150Relay(waitResponseParsePro, receiveBytes, receiveLen));
            } else if (processStrategy instanceof ZBSensor) {//zigbee传感器数据是主动上报，所以不使用回调，而是缓存到list中
                dataRep.addAll(processStrategy.process(receiveBytes, receiveLen));
            } else {
                dataProtocols4CallBack.addAll(processStrategy.process(receiveBytes, receiveLen));
            }
        }

        List<CommonListener> copyCommonListeners = new ArrayList<>(commonListeners.get());
        for (DataProtocol dataProtocol : dataProtocols4CallBack) {
            for (CommonListener commonListener : copyCommonListeners) {
                if (dataProtocol != null) {
                    handler.post(() -> commonListener.onResponse(dataProtocol));
                }
            }
        }
    }

    void printLogData(byte[] receiveBytes, int len, String logPre) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < len; i++) {
            byte itemByte = receiveBytes[i];
            String hexString = Integer.toHexString(itemByte & 0xff);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            stringBuffer.append(hexString).append(" ");
        }
        Log.e(logPre + ">>>", stringBuffer.toString());
    }

    abstract protected void startWriteThread();

    abstract protected void startReadThread();


    @Override
    public void sendProtocol(DataProtocol dataProtocol) {
        waitingCommand.add(dataProtocol);
    }

    @Override
    public void setCommonListener(CommonListener commonListener) {
        commonListeners.add(commonListener);
    }

    @Override
    public void removeCommonListener(CommonListener commonListener) {
        commonListeners.remove(commonListener);

    }

    @Override
    public void removeAllListener() {
        commonListeners.get().clear();
    }

    /**
     * 响应报文需要根据请求报文来解析的，用此方法。
     * 比如modbus4150控制继电器报文
     *
     * @param dataProtocol 请求协议实体，该协议实体将在CommonListener接口回调中被返回。
     */
    @Override
    public void sendProtocolAsRequestFormat(DataProtocol dataProtocol) {
        sendProtocol(dataProtocol);
        waitResponseParsePro.add(dataProtocol);
    }

    @Override
    public DataBus addProcessStrategy(DataProcess dataProcess) {
        this.dataProcesses.add(dataProcess);
        return this;
    }
}

package com.nle.mylibrary.forUse.rfid;

import com.nle.mylibrary.processStrategy.rfid.M100Failed;
import com.nle.mylibrary.processStrategy.rfid.M100ReadUserArea;
import com.nle.mylibrary.processStrategy.rfid.M100ScanEpcCode;
import com.nle.mylibrary.processStrategy.rfid.M100SelectCardByEpc;
import com.nle.mylibrary.processStrategy.rfid.M100SetSelectMode;
import com.nle.mylibrary.processStrategy.rfid.M100WriteUserArea;
import com.nle.mylibrary.processStrategy.rfid.UHFEpcCode;
import com.nle.mylibrary.processStrategy.rfid.UHFReadUserArea;
import com.nle.mylibrary.processStrategy.rfid.UHFWriteUserArea;
import com.nle.mylibrary.protocolEntity.rfid.M100ReadUserAreaDataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.M100ScanEpcCodesProtocol;
import com.nle.mylibrary.protocolEntity.rfid.M100WriteUserAreaDataProtocol;
import com.nle.mylibrary.forUse.BaseUniversalListener;
import com.nle.mylibrary.forUse.exeception.SerialException;
import com.nle.mylibrary.forUse.exeception.SocketException;
import com.nle.mylibrary.transfer.DataBus;
import com.nle.mylibrary.transfer.SocketDataBus;

public class RFID implements BaseUniversalListener {
    private DataBus dataBus;

    public RFID(DataBus dataBus) {
        this.dataBus = dataBus;
        if (dataBus != null) {
            dataBus.addProcessStrategy(new M100Failed()).addProcessStrategy(new M100ReadUserArea()).addProcessStrategy(new M100ScanEpcCode())
                    .addProcessStrategy(new M100SelectCardByEpc()).addProcessStrategy(new M100SetSelectMode()).addProcessStrategy(new M100WriteUserArea())
                    .addProcessStrategy(new UHFEpcCode()).addProcessStrategy(new UHFReadUserArea()).addProcessStrategy(new UHFWriteUserArea());
            dataBus.setup();
        }
    }

    /**
     * 读取单张RFID卡号
     * @param singleEpcListener 回调监听，取回RFID号
     * @throws Exception
     */
    public void readSingleEpc(com.nle.mylibrary.forUse.rfid.SingleEpcListener singleEpcListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }
        dataBus.setCommonListener(new SingleReadListenerImpl(singleEpcListener, dataBus, 20));
        M100ScanEpcCodesProtocol m100ScanEpcCodesProtocol = new M100ScanEpcCodesProtocol();
        dataBus.sendProtocol(m100ScanEpcCodesProtocol);
    }

    /**
     * 写入用户区数据，只支持4位数字或字母组成的字符串
     * @param dataStr 写入的字符串
     * @param rfidWriteListener
     * @throws Exception
     */
    public void writeData(String dataStr, RFIDWriteListener rfidWriteListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }
        char[] data = dataStr.toCharArray();
        if (data.length > 4) {
            throw new SerialException("writing data max len is 4");
        }
        byte[] sendData = new byte[4];

        for (int i = 0; i < 4; i++) {
            if (i > data.length - 1) {
                sendData[i] = 32;//空格的ASCII码
            } else {
                sendData[i] = (byte) data[i];
            }
        }
        dataBus.setCommonListener(new RfidWirteCommonListenerImpl(rfidWriteListener, dataBus));
        M100WriteUserAreaDataProtocol m100WriteUserAreaDataProtocol = new M100WriteUserAreaDataProtocol(sendData, 4, 0);
        dataBus.sendProtocol(m100WriteUserAreaDataProtocol);
    }

    /**
     * 读取用户区数据
     * @param rfidReadListener
     * @throws Exception
     */
    public void readData(RFIDReadListener rfidReadListener) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }
        RfidReadCommonListenerImpl rfidReadCommonListener = new RfidReadCommonListenerImpl(rfidReadListener, dataBus);
        dataBus.setCommonListener(rfidReadCommonListener);
        M100ReadUserAreaDataProtocol m100ReadUserAreaDataProtocol = new M100ReadUserAreaDataProtocol(0, 4);
        dataBus.sendProtocol(m100ReadUserAreaDataProtocol);
    }


    @Override
    public void stopConnect() {
        dataBus.close();
    }

}

package com.nle.mylibrary.forUse.led;

import com.nle.mylibrary.protocolEntity.led.SendTxtProtocol;
import com.nle.mylibrary.protocolEntity.led.SwitchProtocol;
import com.nle.mylibrary.enums.led.DisplayColor;
import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.BaseUniversalListener;
import com.nle.mylibrary.forUse.exeception.SerialException;
import com.nle.mylibrary.forUse.exeception.SocketException;
import com.nle.mylibrary.transfer.DataBus;
import com.nle.mylibrary.transfer.SocketDataBus;

public class LedScreen implements BaseUniversalListener {
    private DataBus dataBus;

    public LedScreen(DataBus dataBus) {
        this.dataBus = dataBus;
        if (dataBus != null) {
            dataBus.setup();
        }
    }

    /**
     * 发送文本到LED屏
     * @param txt 文本内容
     * @param playType1 播放方式
     * @param showSpeed 播放速度
     * @param stopTime 停止时间
     * @param validTime 有效时间，大于99表示永久有效
     * @throws Exception
     */
    public void sendTxt(String txt, PlayType playType1, ShowSpeed showSpeed, int stopTime, int validTime) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }
        dataBus.sendProtocol(new SendTxtProtocol(false, playType1, showSpeed, stopTime, DisplayColor.RED, validTime, txt));
    }

    /**
     * 开关LED屏
     * @param isOpen true开，false关
     * @throws Exception
     */
    public void switchLed(boolean isOpen) throws Exception {
        if (!dataBus.openSuccess()) {
            throw dataBus instanceof SocketDataBus ? new SocketException("tcp尚未连接") : new SerialException("串口尚未打开");
        }

        dataBus.sendProtocol(new SwitchProtocol(isOpen ? 1 : 0));
    }

    @Override
    public void stopConnect() {
        dataBus.close();
    }
}

package com.nle.mylibrary.protocolEntity.led;


import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.enums.led.DisplayColor;
import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;

import java.io.UnsupportedEncodingException;


public class SendTxtProtocol extends DataProtocol {
    private boolean saveTxt;
    private byte playType1;
    private byte showSpeed;
    private byte stopTime;
    private byte displayColor;
    private byte dataValidTime;
    private byte[] txt;

    public SendTxtProtocol() {
    }

    @Override
    public byte[] buildRequestCommand() {
        byte[] preFormat;
        byte[] propertyArea;
        if (saveTxt) {
            preFormat = new byte[]{(byte) 0xAA, 0x01, (byte) 0xBB, 0x51, 0x44};
            propertyArea = new byte[]{playType1, showSpeed, stopTime, displayColor};
        } else {
            preFormat = new byte[]{(byte) 0xAA, 0x01, (byte) 0xBB, 0x51, 0x54};
            propertyArea = new byte[]{playType1, showSpeed, stopTime, displayColor, dataValidTime};
        }
        byte[] dataArea = new byte[propertyArea.length + txt.length];
        System.arraycopy(propertyArea, 0, dataArea, 0, propertyArea.length);
        System.arraycopy(txt, 0, dataArea, propertyArea.length, txt.length);
        byte[] requestCommand = new byte[dataArea.length + preFormat.length + 1 + 1];
        System.arraycopy(preFormat, 0, requestCommand, 0, preFormat.length);
        requestCommand[preFormat.length] = accumulation(dataArea);
        System.arraycopy(dataArea, 0, requestCommand, preFormat.length + 1, dataArea.length);
        requestCommand[requestCommand.length - 1] = (byte) 0xff;
        return requestCommand;
    }

    /**
     *
     * @param saveTxt 目前的LED saveTxt只能为false
     * @param playType
     * @param showSpeed
     * @param stopTime
     * @param displayColor  目前的LED只能显示红色
     * @param dataValidTime
     * @param txt
     * @throws UnsupportedEncodingException
     */
    public SendTxtProtocol(boolean saveTxt, PlayType playType, ShowSpeed showSpeed, int stopTime, DisplayColor displayColor, int dataValidTime, String txt) throws UnsupportedEncodingException {
        this.saveTxt = saveTxt;
        this.playType1 = playType.getCode();
        this.showSpeed = showSpeed.getCode();
        this.stopTime = (byte) (stopTime > 127 ? 127 : stopTime < 0 ? 0 : stopTime);
        this.displayColor = displayColor.getCode();
        this.dataValidTime = (byte) (dataValidTime > 127 ? 127 : dataValidTime < 0 ? 0 : dataValidTime);
        this.txt = txt.getBytes("gb2312");
    }


    @Override
    public void receiveMsg(byte[] data, int len) {
    }

    private byte accumulation(byte[] txtByte) {
        byte result = 0;
        for (byte bte : txtByte) {
            result = (byte) (result + bte);
        }
        return result;
    }
}

package com.nle.mylibrary.transfer;

import android.util.Log;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.serialAbout.SerialPortOp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date:2018/11/2
 * create by pengxl
 */
public class SerialDataBus extends AbsDataBus {
    private String serialName;
    private int baud;
    private int stop;
    private int data;
    private char check;

    private InputStream mInputStream;
    private OutputStream mOutputStream;


    private final int COLLECT_INTERVAL = 25;

    private boolean RECEIVE_TASK_MASK;
    private boolean SEND_MSG_TASK_MASK = true;

    private boolean openResult = false;


    private SerialPortOp serialPort;

    /**
     * @param serialName    串口名称
     * @param baud          波特率
     * @param stop          停止位
     * @param data          数据位
     * @param check         检验位
     */
    SerialDataBus(String serialName, int baud, int stop, int data, char check) {
        this.serialName = serialName;
        this.baud = baud;
        this.stop = stop;
        this.data = data;
        this.check = check;
    }

    @Override
    public void setup() {
        openResult = false;
        File file = new File("/dev/", serialName);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            serialPort = new SerialPortOp(file, baud, stop, data, check, 0);
            mOutputStream = serialPort.getOutputStream();
            mInputStream = serialPort.getInputStream();
            openResult = true;
            startReadThread();
            startWriteThread();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean openSuccess() {
        return openResult;
    }

    @Override
    protected void startWriteThread() {
        new Thread() {
            @Override
            public void run() {
                while (SEND_MSG_TASK_MASK) {
                    DataProtocol dataProtocol = waitingCommand.pop();
                    if (dataProtocol != null) {
                        try {
                            Thread.sleep(100);
                            byte[] command = dataProtocol.buildRequestCommand();
                            if (DEBUG) {
                                printLogData(command, command.length, "writeData");
                            }
                            mOutputStream.write(command);
                        } catch (Exception ignore) {
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    protected void startReadThread() {
        RECEIVE_TASK_MASK = false;
        if (!openResult) {
            Log.e("SerialDataBus", "setup serial failed");
        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(COLLECT_INTERVAL + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    RECEIVE_TASK_MASK = true;
                    while (RECEIVE_TASK_MASK && !isInterrupted()) {
                        try {
                            Thread.sleep(COLLECT_INTERVAL);
                            byte[] receiveBytes = new byte[1024];
                            int receiveLength = mInputStream.read(receiveBytes);
                            if (receiveLength > 0) {
                                conversionData(receiveBytes, receiveLength);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

    }

    @Override
    public void close() {
        RECEIVE_TASK_MASK = false;
        if (serialPort != null) {
            serialPort.close();
        }
        dataRep.get().clear();
        openResult = false;
    }

}

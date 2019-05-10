package com.nle.mylibrary.transfer;

import android.util.Log;

import com.nle.mylibrary.protocolEntity.DataProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * zigbee传感器数据使用列表缓存,针对数据主动上报的缓存在dataRep，获取数据应该从这个队列中取最后的一条匹配数据来返回
 * 而针对非主动上报的，还是使用setCommonListener来监听返回数据
 */
public class SocketDataBus extends AbsDataBus {
    private String ip;
    private int port;

    private boolean RECEIVE_TASK_MASK = false;
    private boolean SEND_MSG_TASK_MASK = false;

    private InputStream socketInputStream;
    private OutputStream socketOutputStream;
    private Socket socket;
    private boolean socketIsOpen = false;

    SocketDataBus(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void setup() {
        socketIsOpen = false;
        new Thread(() -> {
            try {
                socket = new Socket(ip, port);
                socketInputStream = socket.getInputStream();
                socketOutputStream = socket.getOutputStream();
                startWriteThread();
                startReadThread();
                socketIsOpen = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean openSuccess() {
        return socketIsOpen;
    }

    @Override
    protected void startWriteThread() {
        new Thread() {
            @Override
            public void run() {
                SEND_MSG_TASK_MASK = true;
                while (SEND_MSG_TASK_MASK) {
                    DataProtocol dataProtocol = waitingCommand.pop();
                    if (dataProtocol != null) {
                        try {
                            Thread.sleep(100);
                            byte[] command = dataProtocol.buildRequestCommand();
                            if (DEBUG && command != null) {
                                printLogData(command, command.length, "writeData");
                            }
                            DataOutputStream dataOutputStream = new DataOutputStream(socketOutputStream);
                            if (command != null) {
                                dataOutputStream.write(command);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.e("error", ex.getMessage());
                        }
                    }
                }
            }
        }.start();

    }

    @Override
    protected void startReadThread() {
        new Thread() {
            @Override
            public void run() {
                RECEIVE_TASK_MASK = true;
                DataInputStream dataInputStream;
                while (RECEIVE_TASK_MASK) {
                    dataInputStream = new DataInputStream(socketInputStream);
                    try {
                        byte[] receiveBytes = new byte[64];
                        int receiveLength = dataInputStream.read(receiveBytes);
                        if (receiveLength > 0) {
                            conversionData(receiveBytes, receiveLength);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }


    @Override
    public void close() {
        socketIsOpen = false;
        try {
            RECEIVE_TASK_MASK = false;
            SEND_MSG_TASK_MASK = false;
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            socket = null;
            socketInputStream.close();
            socketOutputStream.close();
            dataRep.get().clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

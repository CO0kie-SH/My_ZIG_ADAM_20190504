package com.nle.mylibrary.forUse.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.M100ScanEpcCodesProtocol;
import com.nle.mylibrary.transfer.CommonListener;
import com.nle.mylibrary.transfer.DataBus;
import com.nle.mylibrary.util.DataTools;

class SingleReadListenerImpl implements CommonListener {
        private SingleEpcListener singleEpcListener;
        private DataBus dataBus;
        private int pollTime;

        SingleReadListenerImpl(SingleEpcListener valListener, DataBus dataBus, int pollTime) {
            this.singleEpcListener = valListener;
            this.dataBus = dataBus;
            this.pollTime = pollTime;
        }

        @Override
        public void onResponse(DataProtocol dataProtocol) {
            if (dataProtocol instanceof M100ScanEpcCodesProtocol) {
                M100ScanEpcCodesProtocol m100ScanEpcCodesProtocol = (M100ScanEpcCodesProtocol) dataProtocol;
                if (m100ScanEpcCodesProtocol.getResult().size() > 0) {
                    System.out.println(">>>>>>1");
                    String result = DataTools.formatByteArray(m100ScanEpcCodesProtocol.getResult().get(0));
                    singleEpcListener.onVal(result);
                    dataBus.removeCommonListener(this);
                } else {
                    if (pollTime > 0) {
                        System.out.println(">>>>>>3");
                        pollTime--;
                        M100ScanEpcCodesProtocol resendData = new M100ScanEpcCodesProtocol();
                        dataBus.sendProtocol(resendData);
                    } else {
                        System.out.println(">>>>>>4");
                        dataBus.removeCommonListener(this);
                    }
                }
            }else{
                System.out.println(">>>>>>2");
            }

        }
    }
package com.nle.mylibrary.forUse.rfid;

import com.nle.mylibrary.protocolEntity.DataProtocol;
import com.nle.mylibrary.protocolEntity.rfid.M100ReadUserAreaDataProtocol;
import com.nle.mylibrary.transfer.CommonListener;
import com.nle.mylibrary.transfer.DataBus;

class RfidReadCommonListenerImpl implements CommonListener {
        private RFIDReadListener rfidReadListener;
    private   DataBus dataBus;

         RfidReadCommonListenerImpl(RFIDReadListener rfidReadListener, DataBus dataBus) {
            this.rfidReadListener = rfidReadListener;
             this.dataBus = dataBus;
        }

        @Override
        public void onResponse(DataProtocol dataProtocol) {
            if (dataProtocol instanceof M100ReadUserAreaDataProtocol) {
                M100ReadUserAreaDataProtocol m100ReadUserAreaDataProtocol = (M100ReadUserAreaDataProtocol) dataProtocol;
                if (rfidReadListener != null) {
                    byte[] result = m100ReadUserAreaDataProtocol.getUserData();
                    char[] readableData = new char[result.length];
                    for (int i = 0; i < result.length; i++) {
                        readableData[i] = (char) result[i];
                    }
                    rfidReadListener.onResult(new String(readableData).trim());
                }
                dataBus.removeCommonListener(this);
            }
        }
    }
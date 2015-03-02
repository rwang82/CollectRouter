package com.collectrouter.nwe;

import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by rom on 12/19 0019.
 */
public class HMNWECliRecvDataThread extends Thread{
    HMNWECliImpl mCliImpl;
    public boolean mbNeedExit;
    byte[] mBufRecvData;
    String mstrIPAddr;
    int mnPort;

    HMNWECliRecvDataThread( HMNWECliImpl cliImpl ) {
        mCliImpl = cliImpl;
        mbNeedExit = false;
        mBufRecvData = new byte[ HMNWECliDef.BUFSIZE_RECV_NWDATA ];
        mstrIPAddr = "";
        mnPort = 0;
    }

    void wantExit() {
        mbNeedExit = true;
    }

    @Override
    public void run() {
        int nCopied = 0;

        mstrIPAddr = mCliImpl.mstrIPAddr;
        mnPort = mCliImpl.mnPort;
        mbNeedExit = false;
        while ( !mbNeedExit && mCliImpl.getConnectState() == HMNWECliDef.ENUMSTATUSTYPE.ESTATUS_CONNECTED ) {
            try {
                nCopied = mCliImpl.mSocketIS.read( mBufRecvData );
                if (nCopied == -1) {
                    //
                    doNotifyDisConnect();
                    return; // remote server is close.
                } else if ( nCopied == 0 ) {
                    //
                    doNotifyDisConnect();
                    return;
                } else {
                    assert( nCopied <= HMNWECliDef.BUFSIZE_RECV_NWDATA );
                    byte[] rawData = new byte[ nCopied ];
                    System.arraycopy( mBufRecvData, 0, rawData, 0, nCopied );

                    doNotifyRecvData( rawData, nCopied );
                }
            } catch ( IOException e ) {
                // maybe need do something.

                //
                doNotifyDisConnect();
            }
        }
    }

    private void doNotifyRecvData(byte[] bufRecvData, int nLenCopied ) {
        Message msg = new Message();
        msg.what = HMNWECliDef.MSG_NOTIFY_RECVDATA;
        Bundle bundle = new Bundle();
        bundle.putString( HMNWECliDef.TITLE_CONNECT_IP, mstrIPAddr );
        bundle.putInt( HMNWECliDef.TITLE_CONNECT_PORT, mnPort );
        bundle.putByteArray(HMNWECliDef.TITLE_RECVDATA_BUF, bufRecvData );
        bundle.putInt( HMNWECliDef.TITLE_RECVDATA_LENBUF, nLenCopied );
        msg.setData( bundle );
        mCliImpl.mOutHandler.sendMessage( msg );
    }

    private void doNotifyDisConnect() {
        Message msg = new Message();
        msg.what = HMNWECliDef.MSG_NOTIFY_DISCONNECTED;
        Bundle bundle = new Bundle();
        bundle.putString( HMNWECliDef.TITLE_CONNECT_IP, mstrIPAddr );
        bundle.putInt( HMNWECliDef.TITLE_CONNECT_PORT, mnPort );
        msg.setData( bundle );
        mCliImpl.mHandler.sendMessage(msg);
    }


}

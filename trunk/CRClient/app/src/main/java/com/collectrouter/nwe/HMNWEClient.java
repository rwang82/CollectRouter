package com.collectrouter.nwe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by rom on 12/12 0012.
 */
public class HMNWEClient {
    HMNWECliImpl mImpl;
    Handler mRecvHandler;
    HMNWECliEventHandler mEventHandler;

    public HMNWEClient( HMNWECliEventHandler eventHandler ) {
        //
        mEventHandler = eventHandler;
        mRecvHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch ( msg.what ) {
                    case HMNWECliDef.MSG_ACK_CONNECT: {
                        onMsgAckConnect( msg );
                    }
                    break;
                    case HMNWECliDef.MSG_NOTIFY_DISCONNECTED:{
                        onMsgNotifyDisConnected( msg );
                    }
                    break;
                    case HMNWECliDef.MSG_NOTIFY_RECVDATA:{
                        onMsgNotifyRecvData( msg );
                    }
                    break;
                    default:{

                    }
                }
            }
        };
        mImpl = new HMNWECliImpl( mRecvHandler );
        new Thread( mImpl ).start();
    }

    public void connect( String strIPAddr, int nPort ) {
        Message msg = new Message();
        msg.what = HMNWECliDef.MSG_REQ_CONNECT;
        Bundle bundle = new Bundle();
        bundle.putString( HMNWECliDef.TITLE_CONNECT_IP, strIPAddr);
        bundle.putInt( HMNWECliDef.TITLE_CONNECT_PORT, nPort );
        msg.setData(bundle);
        mImpl.mHandler.sendMessage( msg );
    }

    public void closeConnect() {
        Message msg = new Message();
        msg.what = HMNWECliDef.MSG_REQ_CLOSECONNECT;
        mImpl.mHandler.sendMessage( msg );
    }

    public boolean isConnected() {
        return mImpl.isConnected();
    }

    public boolean sendData( byte[] rawBuf, int nLenRawBuf ) {
        Message msg = new Message();
        msg.what = HMNWECliDef.MSG_REQ_SENDDATA;
        Bundle bundle = new Bundle();
        bundle.putByteArray( HMNWECliDef.TITLE_SENDDATA_RAWDATA, rawBuf );
        bundle.putInt( HMNWECliDef.TITLE_SENDDATA_LENRAWDATA, nLenRawBuf );
        msg.setData( bundle );
        mImpl.mHandler.sendMessage( msg );

        return true;
    }

    void onMsgAckConnect( Message msg ) {
        String strIP = msg.getData().getString( HMNWECliDef.TITLE_CONNECT_IP );
        int nPort = msg.getData().getInt( HMNWECliDef.TITLE_CONNECT_PORT );
        boolean bSuccess = msg.getData().getBoolean( HMNWECliDef.TITLE_CONNECT_RESULT );

        if ( bSuccess ) {
            mEventHandler.onConnected( strIP, nPort );
        } else {
            mEventHandler.onConnectFailed( strIP, nPort );
        }
    }

    private void onMsgNotifyDisConnected( Message msg ) {
        String strIPAddr = msg.getData().getString( HMNWECliDef.TITLE_CONNECT_IP );
        int nPort = msg.getData().getInt( HMNWECliDef.TITLE_CONNECT_PORT );

        // if need new a new message to mOutHandler.
        mEventHandler.onDisConnected( strIPAddr, nPort );
    }

    private void onMsgNotifyRecvData(Message msg) {
        String strIPAddr = msg.getData().getString( HMNWECliDef.TITLE_CONNECT_IP );
        int nPort = msg.getData().getInt( HMNWECliDef.TITLE_CONNECT_PORT );
        byte[] bufRecv = msg.getData().getByteArray( HMNWECliDef.TITLE_RECVDATA_BUF );
        int nLenBuf = msg.getData().getInt( HMNWECliDef.TITLE_RECVDATA_LENBUF );

        mEventHandler.onRecv( strIPAddr, nPort, bufRecv, nLenBuf );
    }
}

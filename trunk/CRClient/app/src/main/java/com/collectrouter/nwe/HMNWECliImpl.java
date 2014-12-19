package com.collectrouter.nwe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by rom on 12/15 0015.
 */
public class HMNWECliImpl implements Runnable {
    public Handler mHandler;
    Handler mOutHandler;
    HMNWECliDef.ENUMSTATUSTYPE meStateCur;
    HMNWECliStateBase mStateCur;
    HMNWECliStateDisConnected mStateDisConnected;
    HMNWECliStateConnecting mStateConnecting;
    HMNWECliStateConnected mStateConnected;
    HMNWECliStateDisConnecting mStateDisConnecting;
    Socket msConnect;
    String mstrIPAddr;
    int mnPort;
    OutputStream mSocketOS;
    InputStream mSocketIS;
    HMNWECliRecvDataThread mThreadRecvData;
    {
        mStateDisConnected = new HMNWECliStateDisConnected( this );
        mStateConnecting = new HMNWECliStateConnecting( this );
        mStateConnected = new HMNWECliStateConnected( this );
        mStateDisConnecting = new HMNWECliStateDisConnecting(this);
        mStateCur = mStateDisConnected;
        meStateCur = mStateCur.getState();
        msConnect = null;
        mstrIPAddr = "";
        mnPort = 0;
        mSocketIS = null;
        mSocketOS = null;
        mThreadRecvData = new HMNWECliRecvDataThread( this );
    }

    HMNWECliImpl( Handler recvHandler ) {
        mOutHandler = recvHandler;
    }

    public boolean isConnected() {
        synchronized ( meStateCur ) {
            return meStateCur == HMNWECliDef.ENUMSTATUSTYPE.ESTATUS_CONNECTED;
        }
    }

    public boolean isDisConnected() {
        synchronized ( meStateCur ) {
            return meStateCur == HMNWECliDef.ENUMSTATUSTYPE.ESTATUS_DISCONNECTED;
        }
    }

    public HMNWECliDef.ENUMSTATUSTYPE getConnectState() {
        synchronized (meStateCur) {
            return meStateCur;
        }
    }

    public void run() {
        Looper.prepare();
        //
        mHandler = new Handler()
        {
            //
            @Override
            public void handleMessage(Message msg)
            {
                switch ( msg.what )
                {
                    case HMNWECliDef.MSG_REQ_CONNECT:
                    {
                        onMsgRegConnect( msg );
                    }
                    break;
                    case HMNWECliDef.MSG_REQ_CLOSECONNECT:
                    {
                        onMsgRegCloseConnect( msg );
                    }
                    break;
                    case HMNWECliDef.MSG_REQ_SENDDATA: {
                        onMsgRegSendData( msg );
                    }
                    break;
                    case HMNWECliDef.MSG_NOTIFY_DISCONNECTED: {
                        onMsgNotifyDisConnected( msg );
                    }
                    break;
                    default: {
                    }
                }
            }
        };

        //
        Looper.loop();
    }


    private void switchState( HMNWECliStateBase stateNew ) {
        synchronized ( meStateCur ) {
            mStateCur = stateNew;
            meStateCur = mStateCur.getState();
        }
    }

    private void onMsgRegSendData(Message msg) {
        byte[] rawBuf = msg.getData().getByteArray( HMNWECliDef.TITLE_SENDDATA_RAWDATA );
        int nLenRawBuf = msg.getData().getInt( HMNWECliDef.TITLE_SENDDATA_LENRAWDATA );
        //
        mStateCur.doSendData( rawBuf, nLenRawBuf );
    }

    private void onMsgRegConnect( Message msg ) {
        String strIPAddr = msg.getData().getString(HMNWECliDef.TITLE_CONNECT_IP);
        int nPort = msg.getData().getInt(HMNWECliDef.TITLE_CONNECT_PORT);
        boolean bSuccess = false;

        //
        if ( mStateCur.getState() != HMNWECliDef.ENUMSTATUSTYPE.ESTATUS_DISCONNECTED ) {
            bSuccess = false;
        } else {
            switchState( mStateConnecting );
            bSuccess = mStateCur.doConnect(strIPAddr, nPort);
            switchState( bSuccess ? mStateConnected : mStateDisConnected );
            if ( bSuccess ) {
                mstrIPAddr = strIPAddr;
                mnPort = nPort;
                mStateCur.doStartRecvData();
            }

        }
        //
        Message newMsg = new Message();
        newMsg.what = HMNWECliDef.MSG_ACK_CONNECT;
        Bundle bundle = new Bundle();
        bundle.putString( HMNWECliDef.TITLE_CONNECT_IP, strIPAddr );
        bundle.putInt( HMNWECliDef.TITLE_CONNECT_PORT, nPort );
        bundle.putBoolean( HMNWECliDef.TITLE_CONNECT_RESULT, bSuccess );
        newMsg.setData( bundle );
        mOutHandler.sendMessage(newMsg);

    }

    private void onMsgRegCloseConnect( Message msg ) {
        String strIPAddr = mstrIPAddr;
        int nPort = mnPort;
        boolean bSuccess = false;

        if( mStateCur.doCloseConnect() ) {
            switchState( mStateDisConnected );
        }
    }

    private void onMsgNotifyDisConnected( Message msg ) {
        String strIPAddr = msg.getData().getString( HMNWECliDef.TITLE_CONNECT_IP );
        int nPort = msg.getData().getInt( HMNWECliDef.TITLE_CONNECT_PORT );

        switchState( mStateDisConnected );
        // if need new a new message to mOutHandler.
        mOutHandler.sendMessage(msg);
    }
}
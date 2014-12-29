package com.collectrouter.nwp;

import com.collectrouter.nwe.HMNWECliEventHandler;

/**
 * Created by rom on 12/23 0023.
 */
public class HMNWPEventAdaptor4Client implements HMNWECliEventHandler {
    private final HMNWPCliEventHandler mEventHandler;
    private final HMNWPUnPackImpl mUnPackHelper;

    public HMNWPEventAdaptor4Client( HMNWPCliEventHandler eventHandler ) {
        mEventHandler = eventHandler;
        mUnPackHelper = new HMNWPUnPackImpl( mEventHandler );
    }

    // interface of HMNWECliEventHandler.
    public void onConnected( String strIPAddr, int nPort ){
        mEventHandler.onConnected( strIPAddr, nPort );
    }

    public void onConnectFailed( String strIPAddr, int nPort ) {
        mEventHandler.onConnectFailed( strIPAddr, nPort );
    }

    public void onDisConnected( String strIPAddr, int nPort ) {
        mEventHandler.onDisConnected( strIPAddr, nPort );
    }

    public void onRecv( String strIPAddr, int nPort, byte[] rawBuf, int nLenRawBuf ) {
        mUnPackHelper.onRecv( rawBuf, nLenRawBuf );
        // mEventHandler.onRecv( strIPAddr, nPort, rawBuf, nLenRawBuf );
    }

}

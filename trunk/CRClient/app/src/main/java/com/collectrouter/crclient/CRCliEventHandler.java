package com.collectrouter.crclient;

import com.collectrouter.nwe.HMNWECliEventHandler;

/**
 * Created by rom on 12/18 0018.
 */
public class CRCliEventHandler implements HMNWECliEventHandler {

    @Override
    public void onConnected(String strIPAddr, int nPort) {
        int a = 0;
    }

    @Override
    public void onConnectFailed(String strIPAddr, int nPort) {
        int a = 0;
    }

    @Override
    public void onDisConnected(String strIPAddr, int nPort) {
        int a = 0;
    }

    @Override
    public void onRecv(String strIPAddr, int nPort, byte[] rawBuf, int nLenRawBuf) {

    }
}

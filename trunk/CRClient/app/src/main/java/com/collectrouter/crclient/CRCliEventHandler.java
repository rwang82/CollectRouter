package com.collectrouter.crclient;

import com.collectrouter.nwp.HMNWPCliEventHandler;

/**
 * Created by rom on 12/30 0030.
 */
public class CRCliEventHandler implements HMNWPCliEventHandler {

    @Override
    public void onConnected(String strIPAddr, int nPort) {
        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_CONNECT_SUCCESS, 0, 0 );
    }

    @Override
    public void onConnectFailed(String strIPAddr, int nPort) {
        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_CONNECT_FAILED, 0, 0 );
    }

    @Override
    public void onDisConnected(String strIPAddr, int nPort) {
        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_DISCONNECTED, 0, 0 );
    }

    @Override
    public void onRecv(byte[] rawBuf, int nLenRawBuf) {
        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_RECVDATA, rawBuf, nLenRawBuf );
    }
}

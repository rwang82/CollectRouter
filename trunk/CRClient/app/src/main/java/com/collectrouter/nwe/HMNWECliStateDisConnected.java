package com.collectrouter.nwe;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by rom on 12/18 0018.
 */
public class HMNWECliStateDisConnected implements HMNWECliStateBase{
    private HMNWECliImpl mCliImpl;
    public HMNWECliStateDisConnected( HMNWECliImpl cliImpl ) {
        mCliImpl = cliImpl;
    }

    @Override
    public Boolean doConnect( String strIP, int nPort )
    {
        return false;
    }

    @Override
    public Boolean doCloseConnect()
    {
        return true;
    }

    @Override
    public HMNWECliDef.ENUMSTATUSTYPE getState()
    {
        return HMNWECliDef.ENUMSTATUSTYPE.ESTATUS_DISCONNECTED;
    }

    @Override
    public Boolean doSendData( byte[] rawBuf, int nLenRawBuf ) {
        return false;
    }

    @Override
    public void doStartRecvData() {
        // need to nothing.
    }
}

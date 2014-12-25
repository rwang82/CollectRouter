package com.collectrouter.nwe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by rom on 12/18 0018.
 */
public class HMNWECliStateConnected implements HMNWECliStateBase {
    HMNWECliImpl mCliImpl;

    HMNWECliStateConnected( HMNWECliImpl cliImpl ) {
        mCliImpl = cliImpl;
    }

    @Override
    public Boolean doConnect( String strIP, int nPort )
    {
        // need more code here.
        return false;
    }

    @Override
    public Boolean doCloseConnect()
    {
        return false;
    }

    @Override
    public HMNWECliDef.ENUMSTATUSTYPE getState()
    {
        return HMNWECliDef.ENUMSTATUSTYPE.ESTATUS_CONNECTED;
    }

    @Override
    public Boolean doSendData( byte[] rawBuf, int nLenRawBuf ) {
        try {
            mCliImpl.mSocketOS.write(rawBuf, 0, nLenRawBuf);
        } catch ( IOException e ) {
            return false;
        }

        return true;
    }

    @Override
    public void doStartRecvData() {
        mCliImpl.doStartRecvThread();
    }
}

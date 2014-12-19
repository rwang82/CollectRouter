package com.collectrouter.nwe;

import java.io.IOException;

/**
 * Created by rom on 12/18 0018.
 */
public class HMNWECliStateDisConnecting implements HMNWECliStateBase{
    HMNWECliImpl mCliImpl;

    HMNWECliStateDisConnecting( HMNWECliImpl cliImpl ) {
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
        try {
            mCliImpl.mThreadRecvData.wantExit();
            mCliImpl.msConnect.close();
        } catch ( IOException e ) {
        }
        return true;
    }

    @Override
    public HMNWECliDef.ENUMSTATUSTYPE getState()
    {
        return HMNWECliDef.ENUMSTATUSTYPE.ESTATUS_DISCONNECTING;
    }

    @Override
    public Boolean doSendData( byte[] rawBuf, int nLenRawBuf ) {
        return false;
    }

    @Override
    public void doStartRecvData() {

    }
}

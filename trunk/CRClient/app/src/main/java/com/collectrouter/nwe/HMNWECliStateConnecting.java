package com.collectrouter.nwe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by rom on 12/18 0018.
 */
public class HMNWECliStateConnecting implements HMNWECliStateBase{

    private HMNWECliImpl mCliImpl;

    HMNWECliStateConnecting( HMNWECliImpl cliImpl ) {
        mCliImpl = cliImpl;
    }

    @Override
    public Boolean doConnect( String strIP, int nPort )
    {
        try {
            mCliImpl.msConnect = new Socket(strIP, nPort);
            mCliImpl.mSocketOS = mCliImpl.msConnect.getOutputStream();
            mCliImpl.mSocketIS = mCliImpl.msConnect.getInputStream();
        } catch ( UnknownHostException e ) {

            return false;
        } catch ( IOException e ) {

            return false;
        }

        mCliImpl.mstrIPAddr = strIP;
        mCliImpl.mnPort = nPort;
        return true;

    }

    @Override
    public Boolean doCloseConnect()
    {
        assert( false );
        return false;
    }

    @Override
    public HMNWECliDef.ENUMSTATUSTYPE getState()
    {
        return HMNWECliDef.ENUMSTATUSTYPE.ESTATUS_CONNECTING;
    }

    @Override
    public Boolean doSendData( byte[] rawBuf, int nLenRawBuf ) {
        return false;
    }

    @Override
    public void doStartRecvData() {

    }

}

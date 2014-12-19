package com.collectrouter.nwe;

/**
 * Created by rom on 12/17 0017.
 */
public interface HMNWECliStateBase {
    //
    public Boolean doConnect( String strIP, int nPort );
    public Boolean doCloseConnect();
    public HMNWECliDef.ENUMSTATUSTYPE getState();
    public Boolean doSendData( byte[] rawBuf, int nLenRawBuf );
    public void doStartRecvData(  );
}

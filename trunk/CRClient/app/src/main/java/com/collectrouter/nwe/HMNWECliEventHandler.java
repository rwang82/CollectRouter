package com.collectrouter.nwe;

/**
 * Created by rom on 12/18 0018.
 */
public interface HMNWECliEventHandler {
    public void onConnected( String strIPAddr, int nPort );
    public void onConnectFailed( String strIPAddr, int nPort );
    public void onDisConnected( String strIPAddr, int nPort );
    public void onRecv( String strIPAddr, int nPort, byte[] rawBuf, int nLenRawBuf );
}

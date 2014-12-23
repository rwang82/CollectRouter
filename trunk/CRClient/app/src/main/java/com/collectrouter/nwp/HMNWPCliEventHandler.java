package com.collectrouter.nwp;

/**
 * Created by rom on 12/23 0023.
 */
public interface HMNWPCliEventHandler {
    public void onConnected( String strIPAddr, int nPort );
    public void onConnectFailed( String strIPAddr, int nPort );
    public void onDisConnected( String strIPAddr, int nPort );
    public void onRecv( String strIPAddr, int nPort, byte[] rawBuf, int nLenRawBuf );
}

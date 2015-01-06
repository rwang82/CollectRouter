package com.collectrouter.crclient.frame;

import com.collectrouter.nwp.HMNWPClient;

/**
 * Created by rom on 1/1 0001.
 */
public class CRCliCommMonitor implements Runnable {
    public boolean mbNeedExit;
    private HMNWPClient mNWPClient;

    public CRCliCommMonitor( HMNWPClient nwpClient ) {
        mNWPClient = nwpClient;
        mbNeedExit = false;
    }
    @Override
    public void run() {
        while ( !mbNeedExit ) {
            if ( !mNWPClient.isConnected() ) {
                mNWPClient.connect( CRCliDef.SERVER_IP_LOGIN, CRCliDef.SERVER_PORT_LOGIN );
            }

            try {
                Thread.sleep( 1000 );
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

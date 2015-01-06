package com.collectrouter.nwp;

import com.collectrouter.nwe.HMNWEClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 12/23 0023.
 */
public class HMNWPClient {
    private HMNWEClient mNWECli;
    private HMNWPEventAdaptor4Client mEventAdaptor;
    private HMNWPCliEventHandler mEventHandler;

    public HMNWPClient( HMNWPCliEventHandler eventHandler ) {
        mEventHandler = eventHandler;
        mEventAdaptor = new HMNWPEventAdaptor4Client( mEventHandler );
        mNWECli = new HMNWEClient( mEventAdaptor );
    }

    public void connect( String strIPAddr, int nPort ) {
        mNWECli.connect( strIPAddr, nPort );
    }

    public void closeConnect() {
        mNWECli.closeConnect();
    }

    public boolean isConnected() {
        return mNWECli.isConnected();
    }

    public boolean sendData( byte[] rawBuf, int nLenRawBuf ) {
        if ( !mNWECli.isConnected() ) {
            return false;
        }
        ArrayList< HMNWPPackage > containerPackage = new ArrayList< HMNWPPackage >();
        if ( !HMNWPPackImpl.createPackages( rawBuf, nLenRawBuf, containerPackage ) )
            return false;

        for ( HMNWPPackage nwpPackage : containerPackage ) {
            mNWECli.sendData( nwpPackage.getBufData(), nwpPackage.getLenBufData() );
        }
        return true;
    }

}

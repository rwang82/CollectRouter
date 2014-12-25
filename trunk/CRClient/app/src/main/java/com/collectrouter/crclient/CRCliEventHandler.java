package com.collectrouter.crclient;

import android.widget.TextView;

import com.collectrouter.nwe.HMNWECliEventHandler;
import com.collectrouter.nwp.HMNWPCliEventHandler;

/**
 * Created by rom on 12/18 0018.
 */
public class CRCliEventHandler implements HMNWPCliEventHandler {
    private final CRCliRoot mCRCliRoot;
//public class CRCliEventHandler implements HMNWECliEventHandler {

    CRCliEventHandler( CRCliRoot crCliRoot ) {
        mCRCliRoot = crCliRoot;
    }

    @Override
    public void onConnected(String strIPAddr, int nPort) {
        TextView tvConnStatus = (TextView)mCRCliRoot.mSocketTestActivity.findViewById( R.id.tvConnStatus );

        tvConnStatus.setText( "Connected" );
    }

    @Override
    public void onConnectFailed(String strIPAddr, int nPort){
        TextView tvConnStatus = (TextView)mCRCliRoot.mSocketTestActivity.findViewById( R.id.tvConnStatus );

        tvConnStatus.setText( "Connect Failed" );
    }

    @Override
    public void onDisConnected(String strIPAddr, int nPort) {
        TextView tvConnStatus = (TextView)mCRCliRoot.mSocketTestActivity.findViewById( R.id.tvConnStatus );

        tvConnStatus.setText( "DisConnected" );
    }

    @Override
    public void onRecv(String strIPAddr, int nPort, byte[] rawBuf, int nLenRawBuf){
        TextView tvConnStatus = (TextView)mCRCliRoot.mSocketTestActivity.findViewById( R.id.tvConnStatus );

        tvConnStatus.setText( "Recv Data" );
    }
}

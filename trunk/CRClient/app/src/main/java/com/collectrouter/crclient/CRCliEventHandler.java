package com.collectrouter.crclient;

import android.widget.TextView;

import com.collectrouter.nwe.HMNWECliEventHandler;
import com.collectrouter.nwp.HMNWPCliEventHandler;

import java.io.UnsupportedEncodingException;

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
    public void onRecv(byte[] rawBuf, int nLenRawBuf){
        TextView tvConnStatus = (TextView)mCRCliRoot.mSocketTestActivity.findViewById( R.id.tvConnStatus );
        TextView tvMsgRecv = (TextView)mCRCliRoot.mSocketTestActivity.findViewById( R.id.tvMsgRecv );
        String strMsg;
        try {
            strMsg = new String( rawBuf, "UTF8" );
            tvMsgRecv.setText( strMsg );
        } catch (UnsupportedEncodingException e) {
            int a = 0;
            tvMsgRecv.setText( "recv data encode format error." );
            e.printStackTrace();
        }

        tvConnStatus.setText( "Recv Data" );
    }
}

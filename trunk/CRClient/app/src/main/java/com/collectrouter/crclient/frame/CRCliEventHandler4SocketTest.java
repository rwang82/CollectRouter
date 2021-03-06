package com.collectrouter.crclient.frame;

import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.nwp.HMNWPCliEventHandler;

import java.io.UnsupportedEncodingException;

/**
 * Created by rom on 12/18 0018.
 */
public class CRCliEventHandler4SocketTest implements HMNWPCliEventHandler {
//    private final CRCliRoot mCRCliRoot;
////public class CRCliEventHandler4SocketTest implements HMNWECliEventHandler {
//
//    CRCliEventHandler4SocketTest( CRCliRoot crCliRoot ) {
//        mCRCliRoot = crCliRoot;
//    }

    @Override
    public void onConnected(String strIPAddr, int nPort) {
        TextView tvConnStatus = (TextView)CRCliRoot.getInstance().mSocketTestActivity.findViewById( R.id.tvConnStatus );

        tvConnStatus.setText( "Connected" );
    }

    @Override
    public void onConnectFailed(String strIPAddr, int nPort){
        TextView tvConnStatus = (TextView)CRCliRoot.getInstance().mSocketTestActivity.findViewById( R.id.tvConnStatus );

        tvConnStatus.setText( "Connect Failed" );
    }

    @Override
    public void onDisConnected(String strIPAddr, int nPort) {
        TextView tvConnStatus = (TextView)CRCliRoot.getInstance().mSocketTestActivity.findViewById( R.id.tvConnStatus );

        tvConnStatus.setText( "DisConnected" );
    }

    @Override
    public void onRecv(byte[] rawBuf, int nLenRawBuf){
        TextView tvConnStatus = (TextView)CRCliRoot.getInstance().mSocketTestActivity.findViewById( R.id.tvConnStatus );
        TextView tvMsgRecv = (TextView)CRCliRoot.getInstance().mSocketTestActivity.findViewById( R.id.tvMsgRecv );
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

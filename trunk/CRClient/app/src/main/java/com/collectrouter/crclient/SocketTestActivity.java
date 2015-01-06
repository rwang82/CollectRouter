package com.collectrouter.crclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by rom on 12/24 0024.
 */
public class SocketTestActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_test);

        //
        CRCliRoot.getInstance().mUIDepot.regActivity( CRCliDef.CRCLI_ACTIVITY_SOCKETTEST, this );

        findViewById(R.id.btn_connect).setOnClickListener( onBtnConnect );
        findViewById( R.id.btn_send ).setOnClickListener( onBtnSend );

    }

    @Override
    protected void onStop() {
        CRCliRoot.getInstance().mUIDepot.unRegActivity( CRCliDef.CRCLI_ACTIVITY_SOCKETTEST );

    }

    private View.OnClickListener onBtnConnect = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText etIPAddr = (EditText)findViewById( R.id.etIPAddr );
            String strIPAddr = etIPAddr.getText().toString();
            EditText etPort = (EditText)findViewById( R.id.etPort );
            // etPort.getText().toString()
            //int nPort = etPort.getText().

            CRCliRoot.getInstance().mNWPClient.connect( strIPAddr, 7654 );
        }
    };

    private View.OnClickListener onBtnSend = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText etMsg = (EditText)findViewById( R.id.etMsg );
            String strMsg = etMsg.getText().toString();

            byte[] rawBuf = strMsg.getBytes();
            CRCliRoot.getInstance().mNWPClient.sendData( rawBuf, rawBuf.length );
        }
    };
}

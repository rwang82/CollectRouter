package com.collectrouter.crclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import com.collectrouter.nwe.HMNWECliEventHandler;
import com.collectrouter.nwe.HMNWEClient;
import com.collectrouter.nwp.HMNWPClient;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;


public class LoginActivity extends ActionBarActivity {

    // HMNWPClient mNWPClient;
    HMNWEClient mNWPClient;
    //CRCliEventHandler mCRCliEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //

        //mNWPClient = new HMNWPClient( new CRCliEventHandler() );
        mNWPClient = new HMNWEClient( new HMNWECliEventHandler() {
            @Override
            public void onConnected(String strIPAddr, int nPort) {
                int a = 0;
            }

            @Override
            public void onConnectFailed(String strIPAddr, int nPort) {
                int a = 0;
            }

            @Override
            public void onDisConnected(String strIPAddr, int nPort) {
                int a = 0;
            }

            @Override
            public void onRecv(String strIPAddr, int nPort, byte[] rawBuf, int nLenRawBuf) {
                int a = 0;
            }
        } );
        //
        findViewById(R.id.btn_scan_qr_code).setOnClickListener( handle4BtnScanQRCode );
        findViewById(R.id.btn_login).setOnClickListener( handle4BtnLogin );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener handle4BtnScanQRCode = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println( "enter handle4BtnScanQRCode ------------" );

            IntentIntegrator integrator = new IntentIntegrator(LoginActivity.this);
            integrator.initiateScan( IntentIntegrator.QR_CODE_TYPES );


           //Intent it = new Intent( "com.google.zxing.client.android.SCAN" );
           //startActivity( it );
//            IntentIntegrator integrator = new IntentIntegrator(ZXingTestActivity.this);
//            integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
        }
    };

    private View.OnClickListener handle4BtnLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println( "enter handle4BtnLogin -----------" );

            EditText etUserName = (EditText)findViewById(R.id.etUserName);
            String strUserName = etUserName.getText().toString();
            EditText etPassword = (EditText)findViewById(R.id.etPassword);
            String strPassword = etPassword.getText().toString();

            if ( !mNWPClient.isConnected() ) {
                mNWPClient.connect( "192.168.31.106", 7654 );
            }

            mNWPClient.sendData(strUserName.getBytes(), strUserName.length());

//            byte[] rawBuf;
//            try {
//                rawBuf = "chilema?".getBytes("UTF-8");
//            } catch ( UnsupportedEncodingException e) {
//                int a = 0;
//                return;
//            } catch ( UnsupportedCharsetException e ) {
//                int a = 0;
//                return;
//            } finally {
//                int a = 0;
//            }
//            //mNWPClient.sendData( rawBuf, rawBuf.length );
            int a = 0;
        }
    };
}

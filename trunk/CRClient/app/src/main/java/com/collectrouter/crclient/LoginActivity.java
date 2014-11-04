package com.collectrouter.crclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abc.def.TestCls1;
import com.google.zxing.integration.android.IntentIntegrator;


public class LoginActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TestCls1 aaa = new TestCls1();
        aaa.doShow();

        //
        findViewById(R.id.btn_scan_qr_code).setOnClickListener( handle4BtnScanQRCode );
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
//            IntentIntegrator integrator = new IntentIntegrator(ZXingTestActivity.this);
//            integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
        }
    };
}

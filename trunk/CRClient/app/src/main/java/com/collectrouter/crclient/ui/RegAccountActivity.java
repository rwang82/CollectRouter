package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.R;

/**
 * Created by rom on 1/1 0001.
 */
public class RegAccountActivity extends Activity {

    private View.OnClickListener handler4BtnRegAccount = new View.OnClickListener(){


        @Override
        public void onClick(View v) {
            EditText etUserName = (EditText)findViewById( R.id.etUserName );
            String strUserName = etUserName.getText().toString();
            EditText etPassword = (EditText)findViewById( R.id.etPassword );
            String strPassword = etPassword.getText().toString();
            EditText etPhoneNum = (EditText)findViewById( R.id.etPhoneNum );
            String strPhoneNum = etPhoneNum.getText().toString();

            Bundle bundle = new Bundle();
            bundle.putString( "username", strUserName );
            bundle.putString( "password", strPassword );
            bundle.putString( "phonenum", strPhoneNum );
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_ACCOUNT_REG, bundle, 0 );
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_reg_account);


        CRCliRoot.getInstance().mUIDepot.regActivity( CRCliDef.CRCLI_ACTIVITY_REGACCOUNT, this );
        findViewById( R.id.btn_reg_account ).setOnClickListener( handler4BtnRegAccount );
    }

    @Override
    protected void onDestroy() {
        CRCliRoot.getInstance().mUIDepot.unRegActivity( CRCliDef.CRCLI_ACTIVITY_REGACCOUNT );
        super.onDestroy();
    }
}

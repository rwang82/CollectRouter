package com.collectrouter.crclient.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by rom on 1/6 0006.
 */
public class FragmentLogin extends Fragment {
    public FragmentLogin() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //
        CRCliRoot.getInstance().mUIDepot.regFragment( CRCliDef.CRCLI_FRAGMENT_LOGIN, this );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View viewRoot = inflater.inflate(R.layout.fragment_login, container, false);

        if ( viewRoot == null ) {
            assert( false );
            return null;
        }
        //
        viewRoot.findViewById(R.id.btn_scan_qr_code).setOnClickListener( handle4BtnScanQRCode );
        viewRoot.findViewById(R.id.btn_login).setOnClickListener( handle4BtnLogin );
        viewRoot.findViewById(R.id.btn_goto_reg_account).setOnClickListener( handle4BtnGotoRegAccount );
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mUIDepot.unRegFragment( CRCliDef.CRCLI_FRAGMENT_LOGIN );
        super.onDestroyView();
    }
    private View.OnClickListener handle4BtnScanQRCode = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println( "enter handle4BtnScanQRCode ------------" );

            IntentIntegrator integrator = new IntentIntegrator( getActivity() );
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

            EditText etUserName = (EditText)getActivity().findViewById(R.id.etUserName);
            String strUserName = etUserName.getText().toString();
            EditText etPassword = (EditText)getActivity().findViewById(R.id.etPassword);
            String strPassword = etPassword.getText().toString();

            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_LOGIN, strUserName, strPassword );
        }
    };

    private View.OnClickListener handle4BtnGotoRegAccount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            Intent intent = new Intent( getActivity(), RegAccountActivity.class);
//            startActivityForResult( intent, 0 );
        }
    };
}

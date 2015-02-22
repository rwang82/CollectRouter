package com.collectrouter.crclient.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_LOGIN;

    public FragmentLogin() {

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View viewRoot = inflater.inflate(R.layout.fragment_login, container, false);

        if ( viewRoot == null ) {
            assert( false );
            return null;
        }
        //
        viewRoot.findViewById(R.id.btn_login).setOnClickListener( handle4BtnLogin );
        viewRoot.findViewById(R.id.tv_register_account).setOnClickListener( handle4BtnGotoRegAccount );
        //
        return viewRoot;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

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

            ActivityMain mainActivity = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
            mainActivity.switch2RegAccount();;
//            Intent intent = new Intent( getActivity(), RegAccountActivity.class);
//            startActivityForResult( intent, 0 );
        }
    };

}

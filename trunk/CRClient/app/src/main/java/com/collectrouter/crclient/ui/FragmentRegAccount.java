package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.R;

/**
 * Created by rom on 1/1 0001.
 */
public class FragmentRegAccount extends Fragment {
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_REGACCOUNT;


    private View.OnClickListener handler4BtnRegAccount = new View.OnClickListener(){


        @Override
        public void onClick(View v) {
            Activity activity = getActivity();
            if ( activity == null ) {
                return;
            }

            EditText etUserName = (EditText)activity.findViewById( R.id.etUserName );
            String strUserName = etUserName.getText().toString();
            EditText etPassword = (EditText)activity.findViewById( R.id.etPassword );
            String strPassword = etPassword.getText().toString();
            EditText etPhoneNum = (EditText)activity.findViewById( R.id.etPhoneNum );
            String strPhoneNum = etPhoneNum.getText().toString();
            EditText etNickName = (EditText)activity.findViewById( R.id.etNickName );
            String strNickName = etNickName.getText().toString();
            EditText etEMail = (EditText)activity.findViewById( R.id.etEMail );
            String strEMail = etEMail.getText().toString();

            Bundle bundle = new Bundle();
            bundle.putString( "username", strUserName );
            bundle.putString( "password", strPassword );
            bundle.putString( "phonenum", strPhoneNum );
            bundle.putString( "nickname", strNickName );
            bundle.putString( "email", strEMail );
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_ACCOUNT_REG, bundle, 0 );
        }


    };

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_reg_account, container, false);

        viewRoot.findViewById( R.id.btn_reg_account ).setOnClickListener( handler4BtnRegAccount );

        //
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}

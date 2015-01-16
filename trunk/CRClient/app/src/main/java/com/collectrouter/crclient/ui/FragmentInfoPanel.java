package com.collectrouter.crclient.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRAccountData;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventDepot;
import com.collectrouter.crclient.frame.CREventHandler;

/**
 * Created by apple on 15/1/8.
 */
public class FragmentInfoPanel extends Fragment implements CREventHandler{
    public FragmentInfoPanel() {
        CREventDepot eventDepot = CRCliRoot.getInstance().mEventDepot;
        eventDepot.regEventHandler( CRCliDef.CREVT_START_LOGGING, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_LOGIN_SUCCESS, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_LOGIN_FAILED, this );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_infopanel, container, false);
        //
        viewRoot.setOnTouchListener( mTouchListenerRootView );

        viewRoot.findViewById( R.id.btn_attation ).setOnClickListener( mClickListenerBtnAttation );
        viewRoot.findViewById( R.id.btn_publish ).setOnClickListener(mClickListenerBtnPublish);

        CRCliRoot.getInstance().mUIDepot.regFragment( CRCliDef.CRCLI_FRAGMENT_INFOPANEL, this );

        return viewRoot;

    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mUIDepot.unRegFragment(CRCliDef.CRCLI_FRAGMENT_INFOPANEL);
        super.onDestroyView();
    }

    private View.OnTouchListener mTouchListenerRootView = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    private View.OnClickListener mClickListenerBtnAttation = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            CRCliRoot.getInstance().mEventDepot.fire(CRCliDef.CREVT_BTNCLICK_ENTER_ATTATION, 0, 0);
        }
    };

    private View.OnClickListener mClickListenerBtnPublish = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH, 0, 0 );
        }
    };


    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_START_LOGGING: {
                onEvtStartLoggin();
            }
            break;
            case CRCliDef.CREVT_LOGIN_SUCCESS: {
                onEvtLoginSuccess();
            }
            break;
            case CRCliDef.CREVT_LOGIN_FAILED: {
                onEvtLoginFailed();
            }
            break;
            default:
                break;
        }
    }

    void switchUI2NotLoggedIn() {
        // account name
        TextView tvAccountName = (TextView)getActivity().findViewById(R.id.tv_accountname);
        tvAccountName.setText( CRCliDef.CRSTR_NOT_LOGGED_IN );
        // nick name
        TextView tvNickName = (TextView)getActivity().findViewById( R.id.tv_nickname );
        tvNickName.setText( "" );
        // phone
        TextView tvPhone = (TextView)getActivity().findViewById( R.id.tv_phone );
        tvPhone.setText( "" );
    }

    private void onEvtLoginFailed() {
        switchUI2NotLoggedIn();
    }

    private void onEvtLoginSuccess() {
        CRAccountData accountData = CRCliRoot.getInstance().mAccountData;
        // account name
        TextView tvAccountName = (TextView)getActivity().findViewById( R.id.tv_accountname );
        tvAccountName.setText( accountData.mUserName );
        // nick name
        TextView tvNickName = (TextView)getActivity().findViewById( R.id.tv_nickname );
        tvNickName.setText( accountData.mNickName );
        // phone
        TextView tvPhone = (TextView)getActivity().findViewById( R.id.tv_phone );
        tvPhone.setText( accountData.mPhone );


    }

    private void onEvtStartLoggin() {
        // account name
        TextView tvAccountName = (TextView)getActivity().findViewById(R.id.tv_accountname);
        tvAccountName.setText( CRCliDef.CRSTR_START_LOGGING );
        // nick name
        TextView tvNickName = (TextView)getActivity().findViewById( R.id.tv_nickname );
        tvNickName.setText( "" );
        // phone
        TextView tvPhone = (TextView)getActivity().findViewById( R.id.tv_phone );
        tvPhone.setText( "" );
    }
}

package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_INFOPANEL;

    public FragmentInfoPanel() {
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_infopanel, container, false);
        //
        viewRoot.setOnTouchListener( mTouchListenerRootView );

        //
        viewRoot.findViewById( R.id.tv_attetion ).setOnClickListener( mClickShowAttetionList );
        viewRoot.findViewById( R.id.tv_attetioned ).setOnClickListener( mClickShowAttetionedList );
        //
        viewRoot.findViewById( R.id.tv_do_attation ).setOnClickListener( mClickListenerBtnAttation );
        viewRoot.findViewById( R.id.tv_do_publish ).setOnClickListener(mClickListenerBtnPublish);

        //
        CREventDepot eventDepot = CRCliRoot.getInstance().mEventDepot;
        eventDepot.regEventHandler( CRCliDef.CREVT_START_LOGGING, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_LOGIN_SUCCESS, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_LOGIN_FAILED, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_CUR_LOGIN_ACCOUNT_INFO_UPDATE, this );

        return viewRoot;
    }

    @Override
    public void onDestroyView() {

        //
        CREventDepot eventDepot = CRCliRoot.getInstance().mEventDepot;
        eventDepot.unRegEventHandler( CRCliDef.CREVT_START_LOGGING, this );
        eventDepot.unRegEventHandler( CRCliDef.CREVT_LOGIN_SUCCESS, this );
        eventDepot.unRegEventHandler( CRCliDef.CREVT_LOGIN_FAILED, this );
        eventDepot.unRegEventHandler( CRCliDef.CREVT_CUR_LOGIN_ACCOUNT_INFO_UPDATE, this );


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
            case CRCliDef.CREVT_CUR_LOGIN_ACCOUNT_INFO_UPDATE: {
                onEvtCurLoginAccountInfoUpdate();
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
        // maybe need not, becase will receive event to onEvtCurLoginAccountInfoUpdate.
        // updateCurLoginAccountInfo();
    }

    private void onEvtCurLoginAccountInfoUpdate() {
        updateCurLoginAccountInfo();
    }

    private void updateCurLoginAccountInfo() {
        CRAccountData curAccount = CRCliRoot.getInstance().mData.getCurAccountData();
        if ( curAccount == null )
            return;
        // account name
        TextView tvAccountName = (TextView)getActivity().findViewById( R.id.tv_accountname );
        tvAccountName.setText( curAccount.mUserName );
        // nick name
        TextView tvNickName = (TextView)getActivity().findViewById( R.id.tv_nickname );
        tvNickName.setText( curAccount.mNickName );
        // phone
        TextView tvPhone = (TextView)getActivity().findViewById( R.id.tv_phone );
        tvPhone.setText( curAccount.mPhone );
        // attetioned.
        TextView tvAttetioned = (TextView)getActivity().findViewById( R.id.tv_attetioned );
        tvAttetioned.setText( "attetioned("+curAccount.mCountAttetioned+")" );
        // attetion.
        TextView tvAttetion = (TextView)getActivity().findViewById( R.id.tv_attetion );
        tvAttetion.setText( "attetion("+curAccount.mCountAttetion+")" );
        // published.
        TextView tvPublished = (TextView)getActivity().findViewById( R.id.tv_publish );
        tvPublished.setText( "published("+curAccount.mCountPublished+")" );
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

    private View.OnClickListener mClickShowAttetionList = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ActivityMain activeMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity(CRCliDef.CRCLI_ACTIVITY_MAIN );

            activeMain.closeDrawer();
            //
            activeMain.switch2ShowAttetions();
        }
    };

    private View.OnClickListener mClickShowAttetionedList = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ActivityMain activeMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity(CRCliDef.CRCLI_ACTIVITY_MAIN );

            activeMain.closeDrawer();
            //
            activeMain.switch2ShowAttetioneds();
        }
    };
}

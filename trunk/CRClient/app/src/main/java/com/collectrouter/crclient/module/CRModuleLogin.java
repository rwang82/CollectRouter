package com.collectrouter.crclient.module;

import android.app.Activity;
import android.app.AlertDialog;

import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventDepot;
import com.collectrouter.crclient.frame.CREventHandler;
import com.collectrouter.crclient.frame.CRRMsgHandlerDepot;
import com.collectrouter.crclient.frame.CRRMsgJson;
import com.collectrouter.crclient.frame.CRRMsgJsonHandlerBase;
import com.collectrouter.crclient.ui.ActivityMain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rom on 12/30 0030.
 */
public class CRModuleLogin implements CREventHandler, CRRMsgJsonHandlerBase {

    public CRModuleLogin ( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_LOGIN, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_REG_ACCOUNT_SUCCESS, this );
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_LOGIN, this );
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_LOGOFF, this );
    }

    private void doLogin( String strUserName, String strPassword ) {

        if ( !CRCliRoot.getInstance().mNWPClient.isConnected() ) {
            return;
        }
        //
        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_START_LOGGING, 0, 0 );
        //
        String strCmd = createLoginCmd( strUserName, strPassword );
        byte[] byteCmd = strCmd.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( byteCmd, byteCmd.length );

    }

    private void fillCRAccountData( JSONObject valParams ) {
        String strTmp;
        int nSortType;

        try {
            strTmp = valParams.getString( "username" );
            CRCliRoot.getInstance().mAccountData.mUserName = strTmp;
            strTmp = valParams.getString( "phone" );
            CRCliRoot.getInstance().mAccountData.mPhone = strTmp;
            strTmp = valParams.getString( "email" );
            CRCliRoot.getInstance().mAccountData.mEMail = strTmp;
            strTmp = valParams.getString( "nickname" );
            CRCliRoot.getInstance().mAccountData.mNickName = strTmp;
            nSortType = valParams.getInt( "sort" );
            CRCliRoot.getInstance().mAccountData.mSortType = nSortType;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onRMsgLoginAck( CRRMsgJson rmsgJson ) {
        JSONObject valParams = null;
        boolean bSuccess = false;
        int nErrCode = 0;

        try {
            valParams = rmsgJson.mJsonRoot.getJSONObject( "params" );
        } catch (JSONException e) {
            return;
        }

        try {
            bSuccess = (valParams.getInt("result") == 1);
            if ( !bSuccess ) {
                nErrCode = valParams.getInt("reason");
            }
        } catch (JSONException e) {
            return;
        }

        //
        if ( bSuccess ) {
            fillCRAccountData( valParams );
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_LOGIN_SUCCESS, 0, 0 );
        } else {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_LOGIN_FAILED, 0, 0 );
        }

        ActivityMain mainActivity = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        if ( mainActivity == null )
            return;
        //long lTId = Thread.currentThread().getId();
        if ( bSuccess ) {
            mainActivity.switch2AttationUsers();
        } else {
            // show a notify dialog.
            new AlertDialog.Builder( mainActivity ).setTitle( "login result" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();
        }
    }

    private void onRMsgLogoffAck( CRRMsgJson rmsgJson ) {

    }

    String createLoginCmd( String strUserName, String strPassword ) {
        String strCmd;

        strCmd = "{\"cmd\":{\"title\":\"req_login\",\"type\":1,\"sn\":34252,\"os\":300,\"ver\":\"0.1\"},\"params\":{\"username\":\""
                + strUserName + "\",\"password\":\""
                + strPassword + "\"}}";
        return  strCmd;
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_BTNCLICK_LOGIN: {
                String strUserName = (String)param1;
                String strPassword = (String)param2;

                doLogin( strUserName, strPassword );
            }
            break;
            case CRCliDef.CREVT_REG_ACCOUNT_SUCCESS: {
                String strUserName = (String)param1;
                String strPassword = (String)param2;

                doLogin( strUserName, strPassword );
            }
            default: {
            }
        }
    }

    @Override
    public void accept(CRRMsgJson rmsg) {
        switch ( rmsg.mCmdType ) {
            case CRCliDef.CRCMDTYPE_ACK_LOGIN: {
                onRMsgLoginAck( rmsg );
            }
            break;
            case CRCliDef.CRCMDTYPE_ACK_LOGOFF:{
                onRMsgLogoffAck( rmsg );
            }
            break;
        }
    }

}

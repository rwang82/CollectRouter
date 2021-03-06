package com.collectrouter.crclient.module;

import android.app.Activity;
import android.app.AlertDialog;

import com.collectrouter.crclient.data.CRProduct;
import com.collectrouter.crclient.frame.CRAccountData;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventDepot;
import com.collectrouter.crclient.frame.CREventHandler;
import com.collectrouter.crclient.frame.CRRMsgHandlerDepot;
import com.collectrouter.crclient.frame.CRRMsgJson;
import com.collectrouter.crclient.frame.CRRMsgJsonHandlerBase;
import com.collectrouter.crclient.frame.CRRMsgMaker;
import com.collectrouter.crclient.ui.ActivityMain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rom on 12/30 0030.
 */
public class CRModuleLogin implements CREventHandler, CRRMsgJsonHandlerBase {

    public CRModuleLogin ( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        //
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_LOGIN, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_REG_ACCOUNT_SUCCESS, this );
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_LOGIN, this );
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_LOGOFF, this );
    }

    public boolean islogined() {
        return CRCliRoot.getInstance().mData.getCurAccountData() != null;
    }

    private void doLogin( String strUserName, String strPassword ) {
        if ( !CRCliRoot.getInstance().mNWPClient.isConnected() ) {
            return;
        }
        //
        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_START_LOGGING, 0, 0 );
        //
        CRRMsgMaker.CRRMsgReq rmsgReq = prepareRMsg( strUserName, strPassword );
        byte[] rawBufRMsg = rmsgReq.mRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );
    }

    private void fillCRAccountData( JSONObject valParams ) {
        String strTmp;
        int nSortType;
        CRAccountData newLoginAccount = new CRAccountData();

        try {
            strTmp = valParams.getString( "username" );
            newLoginAccount.mUserName = strTmp;
            strTmp = valParams.getString( "phone" );
            newLoginAccount.mPhone = strTmp;
            strTmp = valParams.getString( "email" );
            newLoginAccount.mEMail = strTmp;
            strTmp = valParams.getString( "nickname" );
            newLoginAccount.mNickName = strTmp;
            nSortType = valParams.getInt( "sort" );
            newLoginAccount.mSortType = nSortType;
            newLoginAccount.mCountAttetioned = valParams.getInt("attetioned");
            newLoginAccount.mCountAttetion = valParams.getInt("attetion");
            newLoginAccount.mCountPublished = valParams.getInt("published");
            //
            CRCliRoot.getInstance().mData.setCurAccountData( newLoginAccount );
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
            } else {
                fillCRAccountData( valParams );
            }
        } catch (JSONException e) {
            return;
        }

        ActivityMain mainActivity = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        if ( mainActivity == null )
            return;
        //long lTId = Thread.currentThread().getId();
        if ( bSuccess ) {
            //
            mainActivity.switch2ShowAttetions();
            //
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_LOGIN_SUCCESS, 0, 0 );
        } else {
            // show a notify dialog.
            new AlertDialog.Builder( mainActivity ).setTitle( "login result" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_LOGIN_FAILED, 0, 0 );
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


    private CRRMsgMaker.CRRMsgReq prepareRMsg( String strUserName, String strPassword ) {
        JSONObject valParams = new JSONObject();

        try {
            valParams.put( "username", strUserName );
            valParams.put( "password", strPassword );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_LOGIN );
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
                CRAccountData accountData = (CRAccountData)param1;

                doLogin( accountData.mUserName, accountData.mPassword );
            }
            break;
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

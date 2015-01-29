package com.collectrouter.crclient.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import com.collectrouter.crclient.frame.CRAccountData;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventDepot;
import com.collectrouter.crclient.frame.CREventHandler;
import com.collectrouter.crclient.frame.CRRMsgHandlerDepot;
import com.collectrouter.crclient.frame.CRRMsgJson;
import com.collectrouter.crclient.frame.CRRMsgJsonHandlerBase;
import com.collectrouter.crclient.frame.CRRMsgMaker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rom on 1/1 0001.
 */
public class CRModuleAccountReg implements CREventHandler, CRRMsgJsonHandlerBase {
    private CRAccountData mAccountData;

    public CRModuleAccountReg ( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        //
        mAccountData = new CRAccountData();
        //
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_ACCOUNT_REG, this );
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_ACCOUNT_REG, this );


    }

    private String prepareRMsg( CRAccountData regAccountParam ) {
        JSONObject valParams = new JSONObject();

        try {
            valParams.put("username", regAccountParam.mUserName );
            valParams.put("password", regAccountParam.mPassword );
            valParams.put("phone", regAccountParam.mPhone);
            valParams.put("email", regAccountParam.mEMail);
            valParams.put("nickname", regAccountParam.mNickName);
            valParams.put("sort", 100);
        } catch (JSONException e) {
            return null;
        }

        return CRRMsgMaker.createRMsg(valParams, CRCliDef.CRCMDTYPE_REQ_ACCOUNT_REG);
    }

    private void doRegAccount( CRAccountData regAccountParam ) {
        String strRMsg = prepareRMsg( regAccountParam );
        byte[] rawBufRMsg = strRMsg.getBytes();
        Activity activity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );

        if ( !CRCliRoot.getInstance().mNWPClient.isConnected() ) {
            new AlertDialog.Builder( activity ).setMessage("net connect failed.").show();
            // maybe need show a message box.
            return;
        }

        //
        mAccountData = regAccountParam;
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );

    }

    private void onRMsgAccountRegAck( CRRMsgJson rmsgJson ) {

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

        // show a notify dialog.
        Activity activity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        if ( activity == null )
            return;

        new AlertDialog.Builder( activity ).setTitle( "register account result" ).setMessage( bSuccess ? "succeed" : ( "failed, ERRCODE:" + nErrCode ) ).setPositiveButton( "OK", null ).show();

        if ( bSuccess ) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_REG_ACCOUNT_SUCCESS, mAccountData, 0 );
        }
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_BTNCLICK_ACCOUNT_REG:
            {
                Bundle bundle = (Bundle)param1;
                CRAccountData regAccountParam = new CRAccountData();
                regAccountParam.mUserName = bundle.getString( "username" );
                regAccountParam.mPassword = bundle.getString( "password" );
                regAccountParam.mPhone = bundle.getString("phonenum");
                regAccountParam.mNickName = bundle.getString("nickname");
                regAccountParam.mEMail = bundle.getString("email");
                //String
                //
                doRegAccount( regAccountParam );
            }
            break;
            default:
            {

            }
            break;
        }
    }

    @Override
    public void accept(CRRMsgJson rmsg) {
        switch ( rmsg.mCmdType ) {
            case CRCliDef.CRCMDTYPE_ACK_ACCOUNT_REG: {
                onRMsgAccountRegAck(rmsg);
            }
            break;
            default: {
            }
            break;
        }
    }
}

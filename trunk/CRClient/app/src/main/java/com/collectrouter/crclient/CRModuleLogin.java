package com.collectrouter.crclient;

import android.app.Activity;
import android.app.AlertDialog;

import com.google.zxing.client.android.Contents;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by rom on 12/30 0030.
 */
public class CRModuleLogin implements CREventHandler, CRRMsgJsonHandlerBase {

    public CRModuleLogin ( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_LOGIN, this );
        rmsgHandlerDepot.regRMsgHandler( this );
    }

    private void doLogin( String strUserName, String strPassword ) {

        if ( !CRCliRoot.getInstance().mNWPClient.isConnected() ) {
            CRCliRoot.getInstance().mNWPClient.connect( CRCliDef.SERVER_IP_LOGIN, CRCliDef.SERVER_PORT_LOGIN );
        }

        String strCmd = createLoginCmd( strUserName, strPassword );
        byte[] byteCmd = strCmd.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( byteCmd, byteCmd.length );

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

        // show a notify dialog.
        Activity loginActivity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_LOGIN );
        if ( loginActivity == null )
            return;

        new AlertDialog.Builder( loginActivity ).setTitle( "login result" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();

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

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
import com.collectrouter.crclient.frame.CRRMsgMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 2/20 0020.
 */
public class CRModuleAccountInfo implements CREventHandler, CRRMsgJsonHandlerBase {

    public CRModuleAccountInfo( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        //

        //
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_FETCH_ACCOUNTINFO, this );
    }

    private String prepareRMsg( List<String> listAccoutName ) {
        JSONObject valParams = new JSONObject();
        JSONArray valUserNameList = new JSONArray();

        // get valParams.
        for ( String strAccountName : listAccoutName ) {
            valUserNameList.put( strAccountName );
        }
        try {
            valParams.put( "username_list", valUserNameList );
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        //
        return CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_FETCH_ACCOUNTINFO );
    }

    public void doFetchAccountInfo( String strUserName ) {
        //
        List<String> listAccountName = new ArrayList<>();
        listAccountName.add( strUserName );
        //
        doFetchAccountInfo( listAccountName );
    }

    public void doFetchAccountInfo( List<String> listAccountName ) {
        if ( listAccountName.size() == 0 )
            return;
        String strRMsg = prepareRMsg( listAccountName );
        byte[] rawBufRMsg = strRMsg.getBytes();
        Activity activity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );

        if ( !CRCliRoot.getInstance().mNWPClient.isConnected() ) {
            new AlertDialog.Builder( activity ).setMessage("net connect failed.").show();
            return;
        }

        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {

    }

    @Override
    public void accept(CRRMsgJson rmsg) {
        switch ( rmsg.mCmdType ) {
            case CRCliDef.CRCMDTYPE_ACK_FETCH_ACCOUNTINFO:
            {

            }
            break;
            default:
                break;
        }
    }
}

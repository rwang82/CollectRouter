package com.collectrouter.crclient.module;

import android.app.Activity;
import android.app.AlertDialog;

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
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by apple on 15/1/8.
 */
public class CRModuleAttetion implements CREventHandler, CRRMsgJsonHandlerBase {

    public CRModuleAttetion( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        //
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_ENTER_ATTATION, this );
        //
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_ADD_ATTETION, this );
    }

    public boolean addAttetion( String strDestUserName ) {
        Activity activityMain = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        JSONObject valParams = new JSONObject();
        if ( !prepareAddAttetionParam( valParams, strDestUserName ) ) {
            return false;
        }
        String strRMsg = CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_ADD_ATTETION );
        if ( !CRCliRoot.getInstance().mNWPClient.isConnected() ) {
            new AlertDialog.Builder( activityMain ).setMessage("net connect failed.").show();
            return false;
        }
        byte[] rawBufRMsg = strRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );

        return true;
    }

    private boolean prepareAddAttetionParam( JSONObject valParams, String strDestUserName ) {
        if ( !CRCliRoot.getInstance().mModuleLogin.islogined() ) {
            return false;
        }
        try {
            valParams.put( "username", CRCliRoot.getInstance().mData.mCurLoginAccountName );
            valParams.put( "attetion", strDestUserName );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void onBtnClickAttation() {
        ActivityMain activityMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );

        activityMain.switch2DoAttetion();
//        IntentIntegrator integrator = new IntentIntegrator( activityMain );
//        integrator.initiateScan( IntentIntegrator.QR_CODE_TYPES );
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_BTNCLICK_ENTER_ATTATION: {
                onBtnClickAttation();
            }
            break;
            default:
            break;
        }
    }

    @Override
    public void accept(CRRMsgJson rmsg) {
        switch ( rmsg.mCmdType ) {
            case CRCliDef.CRCMDTYPE_ACK_ADD_ATTETION: {
                onRMsgAddAttetion(rmsg);
            }
            break;
            default:
                break;
        }
    }

    private void onRMsgAddAttetion(CRRMsgJson rmsg) {
        boolean bSuccess = false;
        int nErrCode = 0;
        ActivityMain mainActivity = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );

        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject("params");
            bSuccess = valParams.getInt("result") == 1;
            nErrCode = bSuccess ? 0 : valParams.getInt("reason");
            //
            if (bSuccess) {
                String strDestUserName = valParams.getString( "attetion" );
                //
                CRCliRoot.getInstance().mData.refetchCurAccountData();
            } else {
                // show a notify dialog.
                new AlertDialog.Builder( mainActivity ).setTitle( "add attetion result" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

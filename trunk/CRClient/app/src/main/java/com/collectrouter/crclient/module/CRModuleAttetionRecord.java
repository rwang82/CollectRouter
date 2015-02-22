package com.collectrouter.crclient.module;

import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventDepot;
import com.collectrouter.crclient.frame.CREventHandler;
import com.collectrouter.crclient.frame.CRRMsgHandlerDepot;
import com.collectrouter.crclient.frame.CRRMsgJson;
import com.collectrouter.crclient.frame.CRRMsgJsonHandlerBase;

import org.json.JSONObject;

/**
 * Created by rom on 2/20 0020.
 */
public class CRModuleAttetionRecord implements CREventHandler, CRRMsgJsonHandlerBase {

    public CRModuleAttetionRecord( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        //

        //
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_FETCH_ATTETION_LIST, this );
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {

    }

    @Override
    public void accept(CRRMsgJson rmsg) {
        switch ( rmsg.mCmdType ) {
            case CRCliDef.CRCMDTYPE_ACK_FETCH_ATTETION_LIST:
            {

            }
            break;
            default:
                break;
        }
    }

    public void doFetchAttetionList( String strAccountName ) {
        //CRCliRoot.getInstance().m
    }

    public void doFetchAttetionedList( String strAccountName ) {

    }



}

package com.collectrouter.crclient.frame;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rom on 12/31 0031.
 */
public class CRRMsgJson extends CRRMsgBase {
    public JSONObject mJsonRoot;
    public int mCmdType;

    public CRRMsgJson( JSONObject jsonRoot ) {
        mJsonRoot = jsonRoot;
        JSONObject valCmd = null;
        try {
            valCmd = jsonRoot.getJSONObject("cmd");
            mCmdType = valCmd.getInt( "type" );
        } catch (JSONException e) {
            mCmdType = CRCliDef.CRCMDTYPE_UNKNOWN;
        }

    }

    @Override
    public void execute( CRRMsgJsonHandlerBase rmsgHandler ) {
        rmsgHandler.accept( this );
    }
}

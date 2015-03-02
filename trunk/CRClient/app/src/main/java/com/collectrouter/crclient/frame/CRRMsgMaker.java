package com.collectrouter.crclient.frame;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rom on 1/1 0001.
 */
public class CRRMsgMaker {
    static int s_idBase = 0;

    public static class CRRMsgReq {
        public CRRMsgReq( String strRMsg, int nSN ) {
            mRMsg = strRMsg;
            mSN = nSN;
        }
        public String mRMsg;
        public int mSN;
    }

    public static CRRMsgReq createRMsg( JSONObject valParams, int nCmdType ) {
        JSONObject jsonRoot = new JSONObject();
        JSONObject valCmd = new JSONObject();
        int nSN = _createSN();

        try {
            valCmd.put("type", nCmdType);
            valCmd.put("sn", nSN);
            valCmd.put("os", CRCliDef.EOS_ANDROID);
            valCmd.put("ver", "0.1");
            jsonRoot.put( "cmd", valCmd );
            jsonRoot.put( "params", valParams );
        } catch (JSONException e) {
            return null;
        }

        String strRMsg = jsonRoot.toString();

        return new CRRMsgReq( strRMsg, nSN );
    }

    private static int _createSN() {
        return s_idBase++;
    }
}

package com.collectrouter.crclient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rom on 1/1 0001.
 */
public class CRRMsgMaker {
    static int s_idBase = 0;

    static String createRMsg( JSONObject valParams, int nCmdType ) {
        JSONObject jsonRoot = new JSONObject();
        JSONObject valCmd = new JSONObject();

        try {
            valCmd.put("type", nCmdType);
            valCmd.put("sn", _createSN());
            valCmd.put("os", CRCliDef.EOS_ANDROID);
            valCmd.put("ver", "0.1");
            jsonRoot.put( "cmd", valCmd );
            jsonRoot.put( "params", valParams );
        } catch (JSONException e) {
            return null;
        }

        String strRMsg = jsonRoot.toString();

        return strRMsg;
    }

    private static int _createSN() {
        return s_idBase++;
    }
}

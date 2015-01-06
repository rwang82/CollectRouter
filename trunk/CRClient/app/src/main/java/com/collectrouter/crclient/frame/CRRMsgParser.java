package com.collectrouter.crclient.frame;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by rom on 12/31 0031.
 */
public class CRRMsgParser implements CREventHandler {
    public CRRMsgParser( CREventDepot eventDepot ) {
        eventDepot.regEventHandler( CRCliDef.CREVT_CONNECT_SUCCESS, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_CONNECT_FAILED, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_DISCONNECTED, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_RECVDATA, this );
    }

    void onEvtRecvData( Object param1, Object param2 ) {
        byte[] rawBuf = (byte[])param1;
        int nLenRawBuf = (int)param2;

        CRRMsgBase rmsgNew = parserRMsg( rawBuf, nLenRawBuf );
        if ( rmsgNew == null ) {
            return; // do nothing.
        }

    }

    boolean isMatchCRRMsgJson( byte[] rawBuf, int nLenRawBuf ) {
        assert( rawBuf!=null && nLenRawBuf > 0 );
        return rawBuf[ 0 ] == (byte)'{';
    }

    boolean isMatchCRRMsgBinary( byte[] rawBuf, int nLenRawBuf ) {
        // need more code here. rom.wang 12.31.2014
        return false;
    }

    CRRMsgBase parserRMsg( byte[] rawBuf, int nLenRawBuf ) {
        boolean bRes = false;
        CRRMsgBase rmsgNew;

        // check if it match CRRMsgJson.
        if ( isMatchCRRMsgJson( rawBuf, nLenRawBuf ) ){
            rmsgNew = createCRRMsgJson( rawBuf, nLenRawBuf );
            if ( rmsgNew != null ) {

                CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_RECVRMSGJONS, rmsgNew, 0 );
                return rmsgNew;
            }
        }

        // check if it match CRRMsgXML.



        // check if it match CRRMsgBinary.
        if ( isMatchCRRMsgBinary( rawBuf, nLenRawBuf ) ) {
            rmsgNew = createCRRMsgBinary( rawBuf, nLenRawBuf );
            if ( rmsgNew != null ) {
                CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_RECVRMSGBINARY, rmsgNew, 0 );
            }
            return rmsgNew;
        }

        //
        assert( false );
        return null;
    }

    private CRRMsgBase createCRRMsgBinary(byte[] rawBuf, int nLenRawBuf) {
        return null;
    }

    private CRRMsgBase createCRRMsgJson(byte[] rawBuf, int nLenRawBuf) {
        String strMsg = null;
        JSONObject jsonRoot = null;

        try {
            strMsg = new String( rawBuf, "UTF8" );
        } catch (UnsupportedEncodingException e) {
            // error format msg.
            return null;
        }

        try {
            jsonRoot = new JSONObject( strMsg );
        } catch (JSONException e) {
            return null; // it's not json format.
        }

        return new CRRMsgJson( jsonRoot );
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_RECVDATA: {
                onEvtRecvData( param1, param2 );
            }
            break;
            default: {
            }
            break;
        }
    }
}

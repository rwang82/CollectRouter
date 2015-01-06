package com.collectrouter.crclient;

/**
 * Created by rom on 12/31 0031.
 */
public class CRRMsgBinary extends CRRMsgBase {
    public CRRMsgBinary(){

    }

    @Override
    public void execute( CRRMsgBinaryHandlerBase rmsgHandler ) {
        rmsgHandler.accept( this );
    }
}

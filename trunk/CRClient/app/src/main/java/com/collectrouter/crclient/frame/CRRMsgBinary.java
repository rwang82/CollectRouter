package com.collectrouter.crclient.frame;

/**
 * Created by rom on 12/31 0031.
 */
public class CRRMsgBinary extends CRRMsgBase {
    public int mCmdType = 0;

    public CRRMsgBinary(){
    }

    @Override
    public void execute( CRRMsgBinaryHandlerBase rmsgHandler ) {
        rmsgHandler.accept( this );
    }
}

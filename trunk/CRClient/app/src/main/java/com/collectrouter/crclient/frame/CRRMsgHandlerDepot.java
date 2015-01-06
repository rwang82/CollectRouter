package com.collectrouter.crclient.frame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 12/31 0031.
 */
public class CRRMsgHandlerDepot implements CREventHandler{
    List< CRRMsgJsonHandlerBase > mContainerRMsgJsonHandler;
    List< CRRMsgBinaryHandlerBase > mContainerRMsgBinaryHandler;

    CRRMsgHandlerDepot( CREventDepot eventDepot )
    {
        eventDepot.regEventHandler( CRCliDef.CREVT_RECVRMSGJONS, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_RECVRMSGBINARY, this );
        mContainerRMsgJsonHandler = new ArrayList<>();
    }

    public void regRMsgHandler( CRRMsgJsonHandlerBase rmsgHandler ) {
        mContainerRMsgJsonHandler.add( rmsgHandler );
    }

    public void unRegRMsgHandler( CRRMsgJsonHandlerBase rmsgHandler ) {
        mContainerRMsgJsonHandler.remove( rmsgHandler );
    }

    public void regRMsgHandler( CRRMsgBinaryHandlerBase rmsgHandler ) {
        mContainerRMsgBinaryHandler.add( rmsgHandler );
    }

    public void unRegRMsgHandler( CRRMsgBinaryHandlerBase rmsgHandler ) {
        mContainerRMsgBinaryHandler.remove( rmsgHandler );
    }

    private void onRMsgJson( CRRMsgJson rmsg ) {
        for ( CRRMsgJsonHandlerBase rmsgHandler : mContainerRMsgJsonHandler ) {
            rmsg.execute( rmsgHandler );
        }
    }

    private void onRMsgBinary( CRRMsgBinary rmsg ) {
        for ( CRRMsgBinaryHandlerBase rmsgHandler : mContainerRMsgBinaryHandler ) {
            rmsg.execute( rmsgHandler );
        }
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_RECVRMSGJONS: {
                onRMsgJson( (CRRMsgJson)param1 );
            }
            break;
            case CRCliDef.CREVT_RECVRMSGBINARY:{
                onRMsgBinary( (CRRMsgBinary)param2 );
            }
            break;
            default:{

            }
            break;
        }
    }
}

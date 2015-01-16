package com.collectrouter.crclient.frame;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by rom on 12/31 0031.
 */
public class CRRMsgHandlerDepot implements CREventHandler{
    Map< Integer, List< CRRMsgJsonHandlerBase > > mMapCmdType2RMsgJsonHandlers;
    Map< Integer, List< CRRMsgBinaryHandlerBase > > mMapCmdType2RMsgBinaryHandlers;

    CRRMsgHandlerDepot( CREventDepot eventDepot )
    {
        eventDepot.regEventHandler( CRCliDef.CREVT_RECVRMSGJONS, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_RECVRMSGBINARY, this );
        mMapCmdType2RMsgJsonHandlers = new Hashtable<>();
        mMapCmdType2RMsgBinaryHandlers = new Hashtable<>();
    }

    public void regRMsgHandler( int nCmdType, CRRMsgJsonHandlerBase rmsgHandler ) {
        synchronized ( mMapCmdType2RMsgJsonHandlers ) {
            List< CRRMsgJsonHandlerBase > containerRMsgHandler = mMapCmdType2RMsgJsonHandlers.get( nCmdType );
            if ( containerRMsgHandler == null ) {
                containerRMsgHandler = new ArrayList<>();
                mMapCmdType2RMsgJsonHandlers.put( nCmdType, containerRMsgHandler );
            }
            assert( containerRMsgHandler != null );
            containerRMsgHandler.add( rmsgHandler );
        }
    }

    public void unRegRMsgHandler( int nCmdType, CRRMsgJsonHandlerBase rmsgHandler ) {
        synchronized ( mMapCmdType2RMsgJsonHandlers ) {
            List< CRRMsgJsonHandlerBase > containerRMsgHandler = mMapCmdType2RMsgJsonHandlers.get( nCmdType );
            if ( containerRMsgHandler == null ) {
                return;
            }
            if ( containerRMsgHandler.contains( rmsgHandler ) ) {
                containerRMsgHandler.remove( rmsgHandler );
            }
        }
    }

    public void regRMsgHandler( int nCmdType, CRRMsgBinaryHandlerBase rmsgHandler ) {
        synchronized ( mMapCmdType2RMsgBinaryHandlers ) {
            List< CRRMsgBinaryHandlerBase > containerRMsgHandler = mMapCmdType2RMsgBinaryHandlers.get( nCmdType );
            if ( containerRMsgHandler == null ) {
                containerRMsgHandler = new ArrayList<>();
                mMapCmdType2RMsgBinaryHandlers.put( nCmdType, containerRMsgHandler );
            }

            assert( containerRMsgHandler != null );
            containerRMsgHandler.add( rmsgHandler );
        }
    }

    public void unRegRMsgHandler( int nCmdType, CRRMsgBinaryHandlerBase rmsgHandler ) {
        synchronized ( mMapCmdType2RMsgBinaryHandlers ) {
            List< CRRMsgBinaryHandlerBase > containerRMsgHandler = mMapCmdType2RMsgBinaryHandlers.get( nCmdType );
            if ( containerRMsgHandler == null ) {
                return;
            }
            if ( containerRMsgHandler.contains( rmsgHandler ) ) {
                containerRMsgHandler.remove( rmsgHandler );
            }
        }
    }

    private void onRMsgJson( CRRMsgJson rmsg ) {
        synchronized( mMapCmdType2RMsgJsonHandlers ) {
            List< CRRMsgJsonHandlerBase > containerRMsgHandler = mMapCmdType2RMsgJsonHandlers.get( rmsg.mCmdType );
            if ( containerRMsgHandler == null )
                return;
            for ( CRRMsgJsonHandlerBase rmsgHandler : containerRMsgHandler ) {
                rmsg.execute( rmsgHandler );
            }
        }
    }

    private void onRMsgBinary( CRRMsgBinary rmsg ) {
        synchronized( mMapCmdType2RMsgBinaryHandlers ) {
            List< CRRMsgBinaryHandlerBase > containerRMsgHandler = mMapCmdType2RMsgBinaryHandlers.get( rmsg.mCmdType );
            if ( containerRMsgHandler == null )
                return;
            for ( CRRMsgBinaryHandlerBase rmsgHandler : containerRMsgHandler ) {
                rmsg.execute( rmsgHandler );
            }
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

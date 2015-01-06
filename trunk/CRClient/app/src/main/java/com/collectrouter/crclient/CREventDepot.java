package com.collectrouter.crclient;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by rom on 12/30 0030.
 */
public class CREventDepot {
    Hashtable mMapEventId2EHContainer;

    {
        mMapEventId2EHContainer = new Hashtable();
    }

    boolean regEventHandler( int nEventId, CREventHandler eventHandler ) {
        Set< CREventHandler > containerEventHandler;
        containerEventHandler = (Set< CREventHandler >)mMapEventId2EHContainer.get( nEventId );

        if ( containerEventHandler == null ) {
            containerEventHandler = new HashSet<CREventHandler>();
            mMapEventId2EHContainer.put( nEventId, containerEventHandler );
        }
        return containerEventHandler.add( eventHandler );
    }

    boolean unRegEventHandler( int nEventId, CREventHandler eventHandler ) {
        Set< CREventHandler > containerEventHandler;
        containerEventHandler = (Set<CREventHandler>)mMapEventId2EHContainer.get( nEventId );

        if( containerEventHandler == null ) {
            return false;
        }
        return containerEventHandler.remove( eventHandler );
    }

    void fire( int nEventId, Object param1, Object param2 ) {
        Set< CREventHandler > containerEventHandler;

        containerEventHandler = (Set<CREventHandler>)mMapEventId2EHContainer.get( nEventId );
        if ( containerEventHandler == null ) {
            return;
        }
        //
        for ( CREventHandler eventHandler : containerEventHandler ) {
            eventHandler.onEvent( nEventId, param1, param2 );
        }
    }
}

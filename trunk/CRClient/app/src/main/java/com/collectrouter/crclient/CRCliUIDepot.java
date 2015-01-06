package com.collectrouter.crclient;

import android.app.Activity;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by rom on 12/30 0030.
 */
public class CRCliUIDepot {
    Map mId2Activity;

    {
        mId2Activity = new Hashtable();
    }

    public boolean regActivity( int nId, Activity activity ) {
        if ( mId2Activity.containsKey( nId ) ) {
            assert( false );
            return false;
        }
        return mId2Activity.put( nId, activity ) == activity;
    }

    public void unRegActivity( int nId ) {
        if ( !mId2Activity.containsKey( nId ) ) {
            return;
        }
        mId2Activity.remove( nId );
    }

    Activity getActivity( int nId ) {
        return (Activity)mId2Activity.get( nId );
    }

    boolean hasActivity( int nId ) {
        return mId2Activity.containsKey( nId );
    }
}

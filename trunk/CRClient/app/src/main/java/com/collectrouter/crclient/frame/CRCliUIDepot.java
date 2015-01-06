package com.collectrouter.crclient.frame;

import android.app.Activity;
import android.app.Fragment;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by rom on 12/30 0030.
 */
public class CRCliUIDepot {
    Map mId2Activity;
    Map mId2Fragment;

    {
        mId2Activity = new Hashtable();
        mId2Fragment = new Hashtable();
    }

    public boolean regFragment( int nId, Fragment fragment ) {
        if ( mId2Fragment.containsKey( nId ) ) {
            assert( false );
            return false;
        }
        return mId2Fragment.put( nId, fragment ) == fragment;
    }

    public void unRegFragment( int nId ) {
        if ( !mId2Fragment.containsKey( nId ) ) {
            return;
        }
        mId2Fragment.remove( nId );
    }

    public Fragment getFragment( int nId ) {
        return (Fragment)mId2Fragment.get( nId );
    }

    public boolean hasFragment( int nId ) {
        return mId2Fragment.containsKey( nId );
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

    public Activity getActivity( int nId ) {
        return (Activity)mId2Activity.get( nId );
    }

    public boolean hasActivity( int nId ) {
        return mId2Activity.containsKey( nId );
    }
}

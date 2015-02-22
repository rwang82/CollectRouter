package com.collectrouter.crclient.frame;

import android.app.Activity;
import android.app.Fragment;

import com.collectrouter.crclient.ui.FragmentDoAttetion;
import com.collectrouter.crclient.ui.FragmentInfoPanel;
import com.collectrouter.crclient.ui.FragmentLogin;
import com.collectrouter.crclient.ui.FragmentMyPublishList;
import com.collectrouter.crclient.ui.FragmentNavigateHeader;
import com.collectrouter.crclient.ui.FragmentDoPublish;
import com.collectrouter.crclient.ui.FragmentRegAccount;
import com.collectrouter.crclient.ui.FragmentShowAccountProduct;
import com.collectrouter.crclient.ui.FragmentShowAttetion;
import com.collectrouter.crclient.ui.FragmentShowAttetioned;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by rom on 12/30 0030.
 */
public class CRCliUIDepot {
    Map mId2Activity;
    Map mTag2Fragment;

    {
        mId2Activity = new Hashtable();
        mTag2Fragment = new Hashtable();
    }

    public boolean regFragment( String tag, Fragment fragment ) {
        if ( mTag2Fragment.containsKey( tag ) ) {
            assert( false );
            return false;
        }
        return mTag2Fragment.put( tag, fragment ) == fragment;
    }

    public void unRegFragment( String tag ) {
        if ( !mTag2Fragment.containsKey( tag ) ) {
            return;
        }
        mTag2Fragment.remove( tag );
    }

    public Fragment getFragment( String tag ) {
        return (Fragment)mTag2Fragment.get( tag );
    }

    public boolean hasFragment( String tag ) {
        return mTag2Fragment.containsKey( tag );
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

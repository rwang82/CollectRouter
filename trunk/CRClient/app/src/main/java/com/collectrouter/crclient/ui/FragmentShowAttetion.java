package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.DataSetObserver;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.adapter.FSAccountsAdapterDummy;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventHandler;
import com.collectrouter.crclient.ui.widget.HMTabH;

import java.util.jar.Attributes;

/**
 * Created by rom on 1/6 0006.
 */
public class FragmentShowAttetion extends FragmentShowAccounts implements CREventHandler {
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_SHOW_ATTETION;

    public FragmentShowAttetion() {
        FSAccountsAdapterDummy adapterDummy = new FSAccountsAdapterDummy();

        setShowAccountsAdapter( adapterDummy );
        setShowProductsAdapter( adapterDummy );
        //
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {

            default:
                break;
        }
    }
}

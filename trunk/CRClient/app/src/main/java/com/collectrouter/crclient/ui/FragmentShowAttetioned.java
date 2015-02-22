package com.collectrouter.crclient.ui;

import com.collectrouter.crclient.adapter.FSAccountsAdapterDummy;
import com.collectrouter.crclient.frame.CRCliDef;

/**
 * Created by rom on 2/13 0013.
 */
public class FragmentShowAttetioned extends FragmentShowAccounts {
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_SHOW_ATTETIONED;

    public FragmentShowAttetioned() {
        FSAccountsAdapterDummy adapterDummy = new FSAccountsAdapterDummy();

        setShowAccountsAdapter( adapterDummy );
        setShowProductsAdapter( adapterDummy );

    }
}

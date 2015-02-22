package com.collectrouter.crclient.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;

/**
 * Created by rom on 1/29 0029.
 */
public class FragmentShowAccountProduct extends Fragment {
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_SHOW_ACCOUNT_PRODUCT_LIST;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_show_product, null );

        //
        String strUserName = getArguments().getString( "username" );

        return viewRoot;
    }

    @Override
    public void onDestroyView() {


        super.onDestroyView();
    }

}

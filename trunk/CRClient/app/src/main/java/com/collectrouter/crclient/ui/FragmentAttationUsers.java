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
 * Created by rom on 1/6 0006.
 */
public class FragmentAttationUsers extends Fragment {
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View viewRoot = inflater.inflate(R.layout.fragment_attation_users, container, false);

        CRCliRoot.getInstance().mUIDepot.regFragment( CRCliDef.CRCLI_FRAGMENT_ATTATION_USERS, this );
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mUIDepot.unRegFragment(CRCliDef.CRCLI_FRAGMENT_ATTATION_USERS);
        super.onDestroyView();
    }

}

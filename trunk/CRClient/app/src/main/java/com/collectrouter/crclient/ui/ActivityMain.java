package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;

/**
 * Created by rom on 1/6 0006.
 */
public class ActivityMain extends ActionBarActivity {

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );
        CRCliRoot.getInstance().mUIDepot.regActivity(CRCliDef.CRCLI_ACTIVITY_MAIN, this );


        //
        createNavigateHeader();
        switch2LoginFragment();


        long lTId = Thread.currentThread().getId();

        int a = 0;
    }

    @Override
    public void onDestroy() {
        CRCliRoot.getInstance().mUIDepot.unRegActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        super.onDestroy();
    }

    public void switch2LoginFragment() {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        args.putString( "username", "wyf" );
        fragment.setArguments( args );
        //
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.content_frame, fragment );
        ft.addToBackStack(null);
        ft.commit();

    }

    public void switch2AttationUsers() {
        FragmentAttationUsers fragment = new FragmentAttationUsers();
        Bundle args = new Bundle();
        args.putString( "username", "wyf" );
        fragment.setArguments( args );

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.content_frame, fragment );
        ft.addToBackStack(null);
        ft.commit();
    }

    public void createNavigateHeader() {
        FragmentNavigateHeader fragment = new FragmentNavigateHeader();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.main_header, fragment );
        ft.commit();
    }
}

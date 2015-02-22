package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;

/**
 * Created by rom on 1/7 0007.
 */
public class FragmentNavigateHeader extends Fragment {
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_NAVIGATEHEADER;

    private View.OnClickListener mClickListenerBtnDrawerSwitch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityMain activityMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
            if ( activityMain == null ) {
                return;
            }
            activityMain.openDrawer();
        }
    };

    public FragmentNavigateHeader() {

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_root_head, container, false);
        if ( null == viewRoot ) {
            return null;
        }
        viewRoot.findViewById( R.id.btn_drawer_switch ).setOnClickListener( mClickListenerBtnDrawerSwitch );
        return viewRoot;

//        Activity activity = getActivity();
//        if ( activity == null )
//            return null;
//        //
//        LinearLayout lineRoot = new LinearLayout( activity );
//        lineRoot.setOrientation( LinearLayout.HORIZONTAL );
//        //
//        mBtnDrawerSwitch = new Button( activity );
//        mBtnDrawerSwitch.setText( ">>" );
//        mBtnDrawerSwitch.setOnClickListener( mClickListenerBtnDrawerSwitch );
//        lineRoot.addView( mBtnDrawerSwitch );
//        //
//        mBtnUserList = new Button( activity );
//        mBtnUserList.setText( "UserList" );
//        mBtnUserList.setOnClickListener( mClickListenerBtnUserList );
//        lineRoot.addView( mBtnUserList );
//        //
//        mBtnSortList = new Button( activity );
//        mBtnSortList.setText( "SortList" );
//        mBtnSortList.setOnClickListener( mClickListenerBtnSortList );
//        lineRoot.addView( mBtnSortList );
//        //
//        mBtnProductList = new Button( activity );
//        mBtnProductList.setText( "Product" );
//        mBtnProductList.setOnClickListener( mClickListenerBtnProductList );
//        lineRoot.addView( mBtnProductList );
//
//        return lineRoot;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        //
    }
}

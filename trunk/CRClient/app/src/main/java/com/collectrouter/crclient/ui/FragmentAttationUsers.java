package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.ui.widget.HMTabH;

import java.util.jar.Attributes;

/**
 * Created by rom on 1/6 0006.
 */
public class FragmentAttationUsers extends Fragment {
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View viewRoot = inflater.inflate(R.layout.fragment_attation_users, container, false);
        ListView lvUsers = (ListView) viewRoot.findViewById( R.id.list_view_attation_users );
        lvUsers.setAdapter( mLVAdapterUser );
        HMTabH tabTop = (HMTabH) viewRoot.findViewById( R.id.tab_top );
        tabTop.setAdapter( mTopTabAdaptor );

        CRCliRoot.getInstance().mUIDepot.regFragment( CRCliDef.CRCLI_FRAGMENT_ATTATION_USERS, this );
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mUIDepot.unRegFragment(CRCliDef.CRCLI_FRAGMENT_ATTATION_USERS);
        super.onDestroyView();
    }

    class CRLVAdapterUser extends BaseAdapter{
        int mCount = 20;

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Activity activity = getActivity();
            if ( activity == null ) {
                return null;
            }

            LinearLayout lineRoot = new LinearLayout( activity );
            lineRoot.setOrientation( LinearLayout.HORIZONTAL);
            //
            ImageView ivUser = new ImageView( activity );
            ivUser.setImageResource( R.drawable.ic_launcher );
            lineRoot.addView( ivUser );
            //
            LinearLayout lineText = new LinearLayout( activity );
            lineText.setOrientation( LinearLayout.VERTICAL );
            lineRoot.addView( lineText );
            //
            TextView tvTitle = new TextView( activity );
            tvTitle.setText( "hello" );
            tvTitle.setTextAppearance( activity, R.style.Base_TextAppearance_AppCompat_Large );
            lineText.addView( tvTitle );
            //
            TextView tvDescribe = new TextView( activity );
            tvDescribe.setText( "describe..." );
            tvDescribe.setTextAppearance( activity, R.style.Base_TextAppearance_AppCompat_Small );
            lineText.addView( tvDescribe );

            //
            return lineRoot;
        }
    }

    private CRLVAdapterUser mLVAdapterUser = new CRLVAdapterUser();


    private HMTabH.TabAdapter mTopTabAdaptor = new HMTabH.TabAdapter() {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public String getItemText(int position) {
            switch ( position ) {
                case 0: {
                    return "显示店铺";
                }
                case 1: {
                    return "显示产品";
                }
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getItemId(int positon) {
            return positon;
        }
    };
}

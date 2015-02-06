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
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.ui.widget.HMTabH;

import java.util.jar.Attributes;

/**
 * Created by rom on 1/6 0006.
 */
public class FragmentShowAttetion extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View viewRoot = inflater.inflate(R.layout.fragment_show_attetion, container, false);
        //
        ListView lvUsers = (ListView) viewRoot.findViewById( R.id.list_view_attation_users );
        lvUsers.setAdapter( mLVAdapterUser );
        //
        HMTabH tabTop = (HMTabH) viewRoot.findViewById( R.id.tab_top );
        tabTop.setAdapter( mTopTabAdaptor );
        tabTop.setOnSelectChangeListener(mTabTopSelectChangeListener);
        //
        ListView lvProductSort = (ListView)viewRoot.findViewById( R.id.lv_product_sort );
        lvProductSort.setAdapter( mLVAdapterProductSort );
        lvProductSort.setOnItemClickListener(mLVProductSortClickListener);

        //
        ListView lvProductList = (ListView)viewRoot.findViewById( R.id.lv_product_list );
        lvProductList.setAdapter( mLVAdapterProductList );

        CRCliRoot.getInstance().mUIDepot.regFragment( CRCliDef.CRCLI_FRAGMENT_SHOW_ATTETION, this );
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mUIDepot.unRegFragment(CRCliDef.CRCLI_FRAGMENT_SHOW_ATTETION);
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

            LayoutInflater lif = activity.getLayoutInflater();
            View viewRoot = lif.inflate(R.layout.lvitem_user, null );

            //
            return viewRoot;
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

    private void switch2ProductShow() {
        getActivity().findViewById( R.id.list_view_attation_users ).setVisibility( View.INVISIBLE );
        getActivity().findViewById( R.id.district_showproduct ).setVisibility( View.VISIBLE );
    }

    private void switch2UserShow() {
        getActivity().findViewById( R.id.list_view_attation_users ).setVisibility( View.VISIBLE );
        getActivity().findViewById( R.id.district_showproduct ).setVisibility( View.INVISIBLE );
    }

    private HMTabH.OnSelectChangeListener mTabTopSelectChangeListener = new HMTabH.OnSelectChangeListener() {
        @Override
        public void onSelectChange(int position) {
            switch ( position ) {
                case 0:
                {
                    FragmentShowAttetion.this.switch2UserShow();;
                }
                break;
                case 1:
                {
                    FragmentShowAttetion.this.switch2ProductShow();
                }
                break;
                default:
                    break;
            }
        }
    };

    private BaseAdapter mLVAdapterProductSort = new BaseAdapter() {
        @Override
        public int getCount() {
            return 5;
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

            LayoutInflater lif = activity.getLayoutInflater();
            View viewRoot = lif.inflate( R.layout.lvitem_product_sort, null );
            //
            TextView tvSortName = (TextView)viewRoot.findViewById( R.id.tv_sort_name );
            tvSortName.setText( getSortName( position ) );

            ListView lvSortName = (ListView)activity.findViewById( R.id.lv_product_sort );
            long nItemId = lvSortName.getSelectedItemId();
            if ( nItemId == position ) {
                int a = 0;
            }

            return viewRoot;
        }

        String getSortName( int position ) {
            switch ( position ) {
                case 0:
                {
                    return "显示全部";
                }
                case 1:
                {
                    return "食品";
                }
                case 2: {
                    return "服装";
                }
                case 3: {
                    return "电脑配件";
                }
                case 4: {
                    return "手机周边";
                }
                default:
                    return "";
            }
        }
    };

    private AdapterView.OnItemClickListener mLVProductSortClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int a = 0;
        }
    };

    private BaseAdapter mLVAdapterProductList = new BaseAdapter() {
        @Override
        public int getCount() {
            return 10;
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

            LayoutInflater lif = activity.getLayoutInflater();
            View viewRoot = lif.inflate( R.layout.lvitem_product, null );

            return viewRoot;
        }
    };
}

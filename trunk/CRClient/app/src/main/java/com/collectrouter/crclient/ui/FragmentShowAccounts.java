package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.adapter.CRShowAccountsAdapterBase;
import com.collectrouter.crclient.adapter.CRShowProductsAdapterBase;
import com.collectrouter.crclient.data.CRProduct;
import com.collectrouter.crclient.frame.CRAccountData;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.ui.widget.HMTabH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 2/10 0010.
 */
public class FragmentShowAccounts extends Fragment {
    private CRShowAccountsAdapterBase mShowAccountsAdapter;
    private CRShowProductsAdapterBase mShowProductsAdapter;
    private boolean mbCallCreateView = false;
    private ENUMSHOWMODE mEShowMode = ENUMSHOWMODE.ESM_BY_ACCOUNTS;

    public enum ENUMSHOWMODE { ESM_BY_ACCOUNTS, ESM_BY_PRODUCTS };

    public FragmentShowAccounts() {
        mShowAccountsAdapter = null;
        mShowProductsAdapter = null;
        mEShowMode = ENUMSHOWMODE.ESM_BY_ACCOUNTS;
    }

    public void setShowAccountsAdapter( CRShowAccountsAdapterBase saAdapter ) {
        mShowAccountsAdapter = saAdapter;
        //
        if ( !mbCallCreateView ) {
            return;
        }
        //
        if ( mEShowMode == ENUMSHOWMODE.ESM_BY_ACCOUNTS ) {

            getActivity().findViewById( R.id.list_view_users ).requestLayout();

            //Invalid
        }
    }

    public void setShowProductsAdapter( CRShowProductsAdapterBase spAdapter ) {
        mShowProductsAdapter = spAdapter;
        //
        if ( !mbCallCreateView ) {
            return;
        }

        if ( mShowProductsAdapter != null ) {
            getActivity().findViewById( R.id.lv_product_sort ).requestLayout();
            //
            ListView lvProducts = (ListView)getActivity().findViewById( R.id.lv_product_list );
            lvProducts.setAdapter( new ListAdapter4ShowProduct( mShowProductsAdapter.getProducts() ) );
        }

        //
        if ( mEShowMode == ENUMSHOWMODE.ESM_BY_PRODUCTS ) {
        }
    }

    private void initUIAdapters( View viewRoot ) {
        if ( !mbCallCreateView )
            return;

        // account.
        ListView lvUsers = null;
        if ( viewRoot != null ) {
            lvUsers = (ListView) viewRoot.findViewById(R.id.list_view_users);
        } else {
            lvUsers = (ListView) getActivity().findViewById(R.id.list_view_users);
        }
        lvUsers.setAdapter( new ListAdapter4ShowAccounts() );
        //lvUsers.setOnItemSelectedListener(mLVUserItemSelectedListener);
        lvUsers.setOnItemClickListener( mLVUserItemClickListener );

        // product sort and list.
        ListView lvProduct = null;
        ListView lvProductSort = null;
        if ( viewRoot != null ) {
            lvProductSort = (ListView) viewRoot.findViewById(R.id.lv_product_sort);
            lvProduct = (ListView)viewRoot.findViewById( R.id.lv_product_list );
        } else {
            lvProductSort = (ListView) getActivity().findViewById(R.id.lv_product_sort);
            lvProduct = (ListView) getActivity().findViewById(R.id.lv_product_list);
        }
        lvProductSort.setAdapter(new ListAdapter4ProductSort());
        lvProductSort.setOnItemClickListener( mLVProductSortClickListener );

        if ( mShowProductsAdapter != null ) {
            lvProduct.setAdapter( new ListAdapter4ShowProduct( mShowProductsAdapter.getProducts( 20 ) ) );
        } else {
            lvProduct.setAdapter( new ListAdapter4ShowProduct( new ArrayList<CRProduct>() ) );
        }

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_show_accounts, container, false);
        //
        HMTabH tabTop = (HMTabH) viewRoot.findViewById( R.id.tab_top );
        tabTop.setAdapter( mTopTabAdaptor );
        tabTop.setOnSelectChangeListener(mTabTopSelectChangeListener);

        //
        mbCallCreateView = true;
        //
        initUIAdapters( viewRoot );
        //
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

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
        public int getItemId(int position) {
            return position;
        }
    };

    private HMTabH.OnSelectChangeListener mTabTopSelectChangeListener = new HMTabH.OnSelectChangeListener() {
        @Override
        public void onSelectChange(int position) {
            switch ( position ) {
                case 0:
                {
                    FragmentShowAccounts.this.switch2UserShow( null );
                }
                break;
                case 1:
                {
                    FragmentShowAccounts.this.switch2ProductShow( null );
                }
                break;
                default:
                    break;
            }
        }
    };

    private void switch2ProductShow( View viewRoot ) {
        ListView lvProductSort = null;
        ListView lvProducts = null;

        mEShowMode = ENUMSHOWMODE.ESM_BY_PRODUCTS;
        //
        if ( viewRoot != null ) {
            viewRoot.findViewById( R.id.list_view_users ).setVisibility( View.INVISIBLE );
            viewRoot.findViewById( R.id.district_showproduct ).setVisibility( View.VISIBLE );
            lvProductSort = (ListView)viewRoot.findViewById( R.id.lv_product_sort );
            lvProducts = (ListView)viewRoot.findViewById( R.id.lv_product_list );
        } else {
            getActivity().findViewById( R.id.list_view_users ).setVisibility( View.INVISIBLE );
            getActivity().findViewById( R.id.district_showproduct ).setVisibility( View.VISIBLE );
            lvProductSort = (ListView)getActivity().findViewById( R.id.lv_product_sort );
            lvProducts = (ListView)getActivity().findViewById(R.id.lv_product_list);
        }
        //
        if ( mShowProductsAdapter != null ) {
            lvProductSort.requestFocusFromTouch();
            lvProductSort.setSelection(0);
        }
    }

    private void switch2UserShow( View viewRoot ) {
        ListView listAccounts = null;
        //
        mEShowMode = ENUMSHOWMODE.ESM_BY_ACCOUNTS;
        //
        if ( viewRoot != null ) {
            //
            listAccounts = (ListView)viewRoot.findViewById(R.id.list_view_users);
            listAccounts.setVisibility(View.VISIBLE);
            viewRoot.findViewById(R.id.district_showproduct).setVisibility( View.INVISIBLE );
        } else {
            //
            listAccounts = (ListView)getActivity().findViewById( R.id.list_view_users );
            listAccounts.setVisibility(View.VISIBLE);
            getActivity().findViewById( R.id.district_showproduct ).setVisibility( View.INVISIBLE );
        }

    }

    protected class ListAdapter4ShowAccounts extends BaseAdapter {
        List< View > mListViewItems;

        public ListAdapter4ShowAccounts() {
            //
            mListViewItems = new ArrayList<>();
            if ( mShowAccountsAdapter != null ) {
                List<CRAccountData> listAccountData = mShowAccountsAdapter.getAccountsData();
                addViewItems( listAccountData.size() );
            }
        }

        @Override
        public int getCount() {
            int nCountNeed = mShowAccountsAdapter!=null ? mShowAccountsAdapter.getAccountCount() : 0 ;
            if ( nCountNeed <= mListViewItems.size() )
                return nCountNeed;
            int nCountNeedAdd = nCountNeed - mListViewItems.size();
            addViewItems( nCountNeedAdd );
            return nCountNeed;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        private void addViewItems( int nCountAdd ) {
            if ( nCountAdd <= 0 )
                return;
            Activity activity = getActivity();
            if ( activity == null ) {
                activity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
                if ( activity == null ) {
                    return;
                }
            }
            //
            LayoutInflater lif = getActivity().getLayoutInflater();
            for ( int nIndex = 0; nIndex < nCountAdd; ++nIndex ) {
                View viewItemRoot = lif.inflate( R.layout.lvitem_user, null );
                mListViewItems.add( viewItemRoot );
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewItemRoot = mListViewItems.get( position );
            //
            if ( mShowAccountsAdapter != null ) {
                CRAccountData accountData = mShowAccountsAdapter.getAccountData( position );
                if ( accountData != null ) {
                    TextView tvNickName = (TextView)viewItemRoot.findViewById( R.id.tv_user_nickname );
                    tvNickName.setText( accountData.mNickName );
                    //
                    TextView tvUserInfo = (TextView)viewItemRoot.findViewById( R.id.tv_user_info );
                    tvUserInfo.setText( accountData.mUserName );
                }
            }

            return viewItemRoot;
        }
    }

    protected class ListAdapter4ProductSort extends BaseAdapter {
        List<View> mListViewItems;

        public ListAdapter4ProductSort() {
            //
            mListViewItems = new ArrayList<>();
            int nSortCount = mShowProductsAdapter == null ? 0 : mShowProductsAdapter.getProductSortCount();
            addViewItems( nSortCount + 1 );
        }

        private void addViewItems( int nCountAdd ) {
            if ( nCountAdd <= 0 )
                return;

            Activity activity = getActivity();
            if ( activity == null )
                activity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
            if ( activity == null ) {
                return;
            }
            LayoutInflater lif = activity.getLayoutInflater();
            for ( int nIndex = 0; nIndex<nCountAdd; ++nIndex ) {
                View viewItemRoot = lif.inflate( R.layout.lvitem_product_sort, null );
                mListViewItems.add( viewItemRoot );
            }
        }

        @Override
        public int getCount() {
            int nSortCount = mShowProductsAdapter == null ? 0 : mShowProductsAdapter.getProductSortCount();
            if ( nSortCount + 1 <= mListViewItems.size() )
                return nSortCount + 1;
            int nItemAdd = nSortCount + 1 - mListViewItems.size();
            addViewItems( nItemAdd );
            return nSortCount + 1;
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
            View viewItem = mListViewItems.get( position );
            String strItemText = null;
            //
            if ( mShowProductsAdapter != null ) {
                TextView tvSortName = (TextView)viewItem.findViewById( R.id.tv_sort_name );
                if ( position == 0 ) {
                    strItemText = "显示全部(" +mShowProductsAdapter.getProducts().size() +")";

                } else {
                    strItemText = mShowProductsAdapter.getProductSortName(position-1) + "("+mShowProductsAdapter.getProductsBySort(position - 1).size()+")";
                }
                tvSortName.setText( strItemText );
            }

            return viewItem;
        }
    }

    protected class ListAdapter4ShowProduct extends BaseAdapter {
        List< CRProduct > mListProducts;
        List< View > mListViewItems;

        public ListAdapter4ShowProduct( List< CRProduct > listProducts ) {
            mListViewItems = new ArrayList<>();
            mListProducts = listProducts;

            int nCount = mListProducts != null ? mListProducts.size() : 0;
            requestViewItem( nCount );
        }

        private void requestViewItem( int nCount ) {
            if ( nCount <= mListViewItems.size() )
                return;
            Activity activity = getActivity();
            if ( activity == null ) {
                activity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
            }
            if ( activity == null )
                return;
            LayoutInflater lif = activity.getLayoutInflater();

            int nNeedAdd = nCount - mListViewItems.size();
            for ( int nIndex = 0; nIndex<nNeedAdd; ++nIndex ) {
                mListViewItems.add( lif.inflate( R.layout.lvitem_product, null ) );
            }

        }

        @Override
        public int getCount() {
            int nCount = mListProducts != null ? mListProducts.size() : 0;
            requestViewItem( nCount );
            return nCount;
        }

        @Override
        public Object getItem(int position) {
            return mListProducts.get( position );
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View viewItemRoot = mListViewItems.get( position );
            CRProduct product = mListProducts.get( position );

            //
            TextView tvTitle = (TextView)viewItemRoot.findViewById( R.id.tv_product_title );
            tvTitle.setText( product.mTitle );
            //
            TextView tvDescribe = (TextView)viewItemRoot.findViewById( R.id.tv_product_describe );
            tvDescribe.setText( product.mDescribe );
            //
        //    TextView tvPrice = (TextView)viewItemRoot.findViewById( R.id.tv_price );
        //    tvPrice.setText( product.mPrice );

            return viewItemRoot;
        }
    }

    private AdapterView.OnItemClickListener mLVProductSortClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lvProducts = (ListView)getActivity().findViewById( R.id.lv_product_list );
            if ( lvProducts == null || position < 0 )
                return;
            if ( mShowProductsAdapter == null )
                return;
            if ( position == 0 ) {
                List< CRProduct > listProducts = mShowProductsAdapter.getProductsBySort( position - 1 );
                lvProducts.setAdapter( new ListAdapter4ShowProduct( mShowProductsAdapter.getProducts() ) );
                parent.requestFocusFromTouch();
                parent.setSelection( position );
            } else {
                List< CRProduct > listProducts = mShowProductsAdapter.getProductsBySort( position - 1 );
                lvProducts.setAdapter( new ListAdapter4ShowProduct( listProducts ) );
                parent.requestFocusFromTouch();
                parent.setSelection( position );
            }
        }
    };

//    private AdapterView.OnItemSelectedListener mLVUserItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            CRAccountData accountDataSel = mShowAccountsAdapter.getAccountData( position );
//            ActivityMain activityMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
//            activityMain.switch2AccountProducts( accountDataSel.mUserName );
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };

    private AdapterView.OnItemClickListener mLVUserItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if ( mShowAccountsAdapter != null ) {
                CRAccountData accountDataSel = mShowAccountsAdapter.getAccountData( position );
                ActivityMain activityMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
                activityMain.switch2AccountProducts( accountDataSel.mUserName );
            }
        }
    };
}

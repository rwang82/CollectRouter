package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.adapter.CRShowProductsAdapterBase;
import com.collectrouter.crclient.adapter.CRShowProductsAdapterDefault;
import com.collectrouter.crclient.data.CRProduct;
import com.collectrouter.crclient.frame.CRAccountData;
import com.collectrouter.crclient.frame.CRCliData;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 1/29 0029.
 */
public class FragmentShowAccountProduct extends Fragment implements CREventHandler {
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_SHOW_ACCOUNT_PRODUCT_LIST;

    private int mSN = CRCliDef.CRCMDSN_INVALID;
    private CRShowProductsAdapterBase mShowProductsAdapter = null;


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
                TextView tvSortName = (TextView)viewItem.findViewById( R.id.tv_sort_name );
                if ( position == 0 ) {
                    strItemText = "显示全部(" + ( ( mShowProductsAdapter != null ) ?mShowProductsAdapter.getProducts().size() : 0 ) +")";

                } else {
                    if ( mShowProductsAdapter != null ) {
                        strItemText = mShowProductsAdapter.getProductSortName(position-1) + "("+mShowProductsAdapter.getProductsBySort(position - 1).size()+")";
                    } else {
                        strItemText = "";
                    }
                }
                tvSortName.setText( strItemText );

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


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_show_product, null );


        CRCliRoot.getInstance().mEventDepot.regEventHandler( CRCliDef.CREVT_FETCH_ACCOUNT_DATA_SUCCESS, this );
        CRCliRoot.getInstance().mEventDepot.regEventHandler( CRCliDef.CREVT_FETCH_ACCOUNT_PRODUCTS_SUCCESS, this );

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

        //
        String strUserName = getArguments().getString( "username" );
        if ( strUserName != null ) {
            mSN = CRCliRoot.getInstance().mData.doFetchAccountData( strUserName );
        }

        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mEventDepot.unRegEventHandler(CRCliDef.CREVT_FETCH_ACCOUNT_DATA_SUCCESS, this);
        CRCliRoot.getInstance().mEventDepot.unRegEventHandler(CRCliDef.CREVT_FETCH_ACCOUNT_PRODUCTS_SUCCESS, this);
        super.onDestroyView();
    }

    private void setShowProductsAdapter( CRShowProductsAdapterBase spAdapter ) {
        mShowProductsAdapter = spAdapter;
        //
        getActivity().findViewById( R.id.lv_product_sort ).requestLayout();
        //
        ListView lvProducts = (ListView)getActivity().findViewById( R.id.lv_product_list );
        lvProducts.setAdapter( new ListAdapter4ShowProduct( mShowProductsAdapter ==  null ? new ArrayList<CRProduct>() : mShowProductsAdapter.getProducts() ) );
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_FETCH_ACCOUNT_DATA_SUCCESS:
            {
                int nSN = (int)param1;
                if ( mSN != nSN )
                    return;
                List<CRAccountData> listAccountData = (List<CRAccountData>)param2;
                if ( listAccountData.size() != 1 )
                    return;
                CRAccountData accountData = listAccountData.get( 0 );

                CRCliData.FAPItem fapParam = new CRCliData.FAPItem( accountData.mUserName, 0, accountData.mCountPublished );
                mSN = CRCliRoot.getInstance().mData.doFetchAccountProducts( fapParam );
            }
            break;
            case CRCliDef.CREVT_FETCH_ACCOUNT_PRODUCTS_SUCCESS:
            {
                int nSN = (int)param1;
                List< String > listAccountName = ( List<String> )param2;
                if ( nSN != mSN )
                    return;

                List< CRProduct > listProducts = CRCliRoot.getInstance().mData.getAccountProducts( listAccountName );
                setShowProductsAdapter(new CRShowProductsAdapterDefault(listProducts));
            }
            break;
            default:
                break;
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
}

package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.data.CRProduct;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.module.CRModulePublish;

/**
 * Created by rom on 1/18 0018.
 */
public class FragmentMyPublishList extends Fragment{

    public FragmentMyPublishList() {
        mLVAdapterUser = new CRLVAdapterMyPublishList( CRCliRoot.getInstance().mModulePublish );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_mypublishlist, container, false);
        ListView lvUsers = (ListView) viewRoot.findViewById( R.id.lv_mypublish_list );
        lvUsers.setAdapter( mLVAdapterUser );

        CRCliRoot.getInstance().mUIDepot.regFragment(CRCliDef.CRCLI_FRAGMENT_MYPUBLISHLIST, this);
        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mUIDepot.unRegFragment( CRCliDef.CRCLI_FRAGMENT_MYPUBLISHLIST );
        super.onDestroyView();
    }

    class CRLVAdapterMyPublishList extends BaseAdapter {
        CRModulePublish mModulePublish;
        CRLVAdapterMyPublishList( CRModulePublish modulePublish ) {
            mModulePublish = modulePublish;
        }

        @Override
        public int getCount() {
            return mModulePublish.getProductPendCount() + mModulePublish.getProductPublishedCount();
        }

        @Override
        public Object getItem(int position) {
            if ( position < 0 ) {
                return null;
            }
            int nPendCount = mModulePublish.getProductPendCount();
            if ( position < nPendCount ) {
                return mModulePublish.getProductPending( position );
            }
            int nPublishCount = mModulePublish.getProductPublishedCount();
            if ( position < nPendCount + nPublishCount ) {
                return mModulePublish.getProductPublished( position - nPendCount );
            }

            assert( false );
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
            int nPendCount = mModulePublish.getProductPendCount();
            int nPublishCount = mModulePublish.getProductPublishedCount();
            CRProduct product = null;
            if ( position < nPendCount ) {
                product = mModulePublish.getProductPending( position );
            } else if ( position < nPendCount + nPublishCount ) {
                product = mModulePublish.getProductPublished( position - nPendCount );
            } else {
                assert( false );
                return null;
            }
            if ( product == null ) {
                assert( false );
                return null;
            }

            View viewRoot = lif.inflate(R.layout.lvitem_mypublish, null );

            //
            ImageView ivPreview = (ImageView) viewRoot.findViewById( R.id.iv_preview );
            ivPreview.setImageResource(R.drawable.ic_launcher);
            //
            TextView tvTitle = (TextView) viewRoot.findViewById( R.id.tv_product_title );
            tvTitle.setText(product.mTitle);
            //
            TextView tvDescribe = (TextView) viewRoot.findViewById( R.id.tv_product_describe );
            tvDescribe.setText( product.mDescribe );
            //
            TextView tvPrice = (TextView) viewRoot.findViewById( R.id.tv_price );
            tvPrice.setText( product.mPrice );
            //
            TextView tvStatus = (TextView) viewRoot.findViewById( R.id.tv_status );
            tvStatus.setText( position < nPendCount ? "pend" : "publish" );

            return viewRoot;
        }
    }

    private CRLVAdapterMyPublishList mLVAdapterUser;
}

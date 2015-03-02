package com.collectrouter.crclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.data.CRProduct;
import com.collectrouter.crclient.frame.CRAccountData;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventHandler;

import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by rom on 1/13 0013.
 */
public class FragmentDoPublish extends Fragment implements CREventHandler {
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_DO_PUBLISH;
    private static final String[] DEFAULT_SORTS = new String[] {
        "食品",
        "服装",
        "电脑配件",
        "手机周边"
    };
    private Map<UUID, Uri> mMapId2Uri;
    private Uri mUriImageCaptureCur;

    public FragmentDoPublish() {
        //
        mMapId2Uri = new Hashtable<>();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_publish, container, false);

        viewRoot.findViewById( R.id.btn_publish_by_photograph ).setOnClickListener( mClickListenerPhotograph );
        viewRoot.findViewById( R.id.btn_publish_by_select_picture ).setOnClickListener( mClickListenerSelectPicture );
        viewRoot.findViewById( R.id.btn_publish_by_scan ).setOnClickListener( mClickListenerScan );
        viewRoot.findViewById( R.id.btn_publish_commit ).setOnClickListener( mClickListenPublishCommit );
        AutoCompleteTextView actvSortType = (AutoCompleteTextView)viewRoot.findViewById( R.id.actv_sort );
        ArrayAdapter<String> adapterSortType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, DEFAULT_SORTS);
        actvSortType.setAdapter( adapterSortType );

        //
        CRCliRoot.getInstance().mEventDepot.regEventHandler( CRCliDef.CREVT_RECV_PHOTOGRAPH_4_PUBLISH, this );
        CRCliRoot.getInstance().mEventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH, this );
        //
        CRCliRoot.getInstance().mUIDepot.regFragment( TAG, this );
        return viewRoot;
    }

    @Override
    public void onDestroyView() {

        //
        CRCliRoot.getInstance().mEventDepot.unRegEventHandler( CRCliDef.CREVT_RECV_PHOTOGRAPH_4_PUBLISH, this );
        CRCliRoot.getInstance().mEventDepot.unRegEventHandler( CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH, this );
        super.onDestroyView();

    }

    private View.OnClickListener mClickListenerPhotograph = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            FragmentDoPublish.this.onBtnClickPhotoGraph4Publish();
        }
    };

    private View.OnClickListener mClickListenerSelectPicture = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_SELECTPICTURE_4_PUBLISH, 0, 0 );

        }
    };

    private View.OnClickListener mClickListenerScan = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_SCAN_4_PUBLISH, 0, 0 );

        }
    };

    private View.OnClickListener mClickListenPublishCommit = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // maybe fill CRProduct code need move to FragmentDoPublish.
            CRProduct product = new CRProduct();
            // get title.
            TextView tvTitle = (TextView)getActivity().findViewById(R.id.et_publish_title);
            product.mTitle = tvTitle.getText().toString();
            // get price.
            TextView tvPrice = (TextView)getActivity().findViewById(R.id.et_publish_price);
            product.mPrice = tvPrice.getText().toString();
            // get describe.
            TextView tvDescribe = (TextView)getActivity().findViewById(R.id.et_publish_product_describe);
            product.mDescribe = tvDescribe.getText().toString();
            // get publisher.
            product.mPublisher = CRCliRoot.getInstance().mData.mCurLoginAccountName;
            // get sort.
            AutoCompleteTextView tvSort = (AutoCompleteTextView)getActivity().findViewById( R.id.actv_sort );
            product.mSort = -1;
            product.mUDSort = tvSort.getText().toString();
            // get images.
            Set<UUID> key = mMapId2Uri.keySet();
            for ( Iterator it = key.iterator(); it.hasNext(); ) {
                UUID imageUUID = (UUID) it.next();
                String strImageUUID = imageUUID.toString();
                product.mImages.add( strImageUUID );
            }
            // get keywords.
            TextView tvKeywords = (TextView)getActivity().findViewById(R.id.et_publish_product_keyword);
            product.mKeywords.add( tvKeywords.getText().toString() );

            //
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_COMMIT_PUBLISH, product, 0 );
        }
    };

    private void reset() {
        mMapId2Uri.clear();
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_RECV_PHOTOGRAPH_4_PUBLISH:
            {
                onRecvPhotograph4Publish( param1, param2 );
            }
            break;
            case CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH:
            {
                onEvtBtnClickEnterPublish();
            }
            default:
                break;
        }
    }

    private void onEvtBtnClickEnterPublish() {
        reset();
    }


    public void onRecvPhotograph4Publish(Object param1, Object param2) {
        int resultCode = (int)param1;
        Intent data = (Intent)param2;
        FragmentDoPublish fragment = (FragmentDoPublish)CRCliRoot.getInstance().mUIDepot.getFragment( CRCliDef.CRCLI_FRAGMENT_DO_PUBLISH );
        Activity activityMain = fragment.getActivity();

        if ( resultCode == 0 ) {
            // means cancel photograph. so need do nothing.
            return;
        }

        if ( data != null ) {
            // maybe you don't do intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // so, have thumbnail.
        } else {
            //
        }

        if ( activityMain == null ) {
            return;
        }

        // get dest imageview size.
        ImageView ivPreview = (ImageView)activityMain.findViewById( R.id.iv_product_preview );
        int nDestWidth = ivPreview.getWidth();
        int nDestHeight = ivPreview.getHeight();
        Bitmap picCapture;

        // get raw picture size
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        try {
            picCapture = BitmapFactory.decodeStream( activityMain.getContentResolver().openInputStream(mUriImageCaptureCur), null, op);
            op.inJustDecodeBounds = false; // set it back.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        //
        int wRatio = op.outWidth/nDestWidth;
        int hRatio = op.outHeight/nDestHeight;
        if ( wRatio > 1 && hRatio > 1 ) {
            op.inSampleSize = ( wRatio > hRatio ) ? wRatio : hRatio;
        }

        //
        try {
            picCapture = BitmapFactory.decodeStream( activityMain.getContentResolver().openInputStream(mUriImageCaptureCur), null, op);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        ivPreview.setImageBitmap( picCapture );
        //
        mMapId2Uri.put( java.util.UUID.randomUUID(), mUriImageCaptureCur );
    }

    private void onBtnClickPhotoGraph4Publish() {
        ActivityMain activityMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        if ( activityMain == null ) {
            return;
        }

        Intent intent;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            intent = new Intent("android.media.action.IMAGE_CAPTURE");
            ContentValues values = new ContentValues(3);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");
            values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            ContentResolver resolver = activityMain.getContentResolver();
            if ( resolver == null ) {
                assert( false );
                return;
            }
//  it let me get into trouble, i can't find it in phone file explore.
//            String tmpFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"123.jpg";
//            File file = new File(tmpFilePath); //创建一个文件
//            mImageFilePathCapture = Uri.fromFile(file);
            mUriImageCaptureCur = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriImageCaptureCur); // now specify the method of saving file and uri to app.
            activityMain.startActivityForResult(intent, CRCliDef.CRREQUESTCODE_PHTOGRAPH4PUBLISH );
        } else {
            Toast.makeText(activityMain, "SD card not ready.", Toast.LENGTH_LONG).show();
        }
    }
}

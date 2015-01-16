package com.collectrouter.crclient.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.data.CRProduct;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventDepot;
import com.collectrouter.crclient.frame.CREventHandler;
import com.collectrouter.crclient.frame.CRRMsgJson;
import com.collectrouter.crclient.frame.CRRMsgJsonHandlerBase;
import com.collectrouter.crclient.frame.CRRMsgMaker;
import com.collectrouter.crclient.ui.ActivityMain;
import com.collectrouter.crclient.ui.FragmentPublish;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by apple on 15/1/8.
 */
public class CRModulePublish implements CREventHandler, CRRMsgJsonHandlerBase {
    private Map<UUID, Uri> mMapId2Uri;
    private Uri mUriImageCaptureCur;
    private Map<Integer, CRProduct > mMapReqCode2ProductPending;
    private List< CRProduct > mContainerProduct;
    private int mReqCodeBase = 0;

    public CRModulePublish( CREventDepot eventDepot ) {
        //
        mMapId2Uri = new Hashtable<>();
        mMapReqCode2ProductPending = new Hashtable<>();
        mContainerProduct = new ArrayList<>();
        //
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_PHOTOGRAPH_4_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_SELECTPICTURE_4_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_SCAN_4_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_COMMIT_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_RECV_PHOTOGRAPH_4_PUBLISH, this );
    }

    private synchronized int allocateReqCode() {
        return mReqCodeBase++;
    }

    private void onBtnClickPublish() {
        ActivityMain activityMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );

        if ( activityMain == null ) {
            return;
        }

        //
        reset();
        //
        activityMain.switch2Publish();
        activityMain.closeDrawer();

    }

    private void reset() {
        mMapId2Uri.clear();

    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH:{
                onBtnClickPublish();
            }
            break;
            case CRCliDef.CREVT_BTNCLICK_PHOTOGRAPH_4_PUBLISH:{
                onBtnClickPhotoGraph4Publish();
            }
            break;
            case CRCliDef.CREVT_BTNCLICK_SELECTPICTURE_4_PUBLISH: {
                onBtnClickSelectPicture4Publish();
            }
            break;
            case CRCliDef.CREVT_BTNCLICK_SCAN_4_PUBLISH: {
                onBtnClickScan4Publish();
            }
            break;
            case CRCliDef.CREVT_BTNCLICK_COMMIT_PUBLISH: {
                onBtnClickCommitPublish();
            }
            break;
            case CRCliDef.CREVT_RECV_PHOTOGRAPH_4_PUBLISH: {
                onRecvPhotograph4Publish( param1, param2 );
            }
            break;
            default:
            break;
        }
    }

    private void onRecvPhotograph4Publish(Object param1, Object param2) {
        int resultCode = (int)param1;
        Intent data = (Intent)param2;
        FragmentPublish fragment = (FragmentPublish)CRCliRoot.getInstance().mUIDepot.getFragment( CRCliDef.CRCLI_FRAGMENT_PUBLISH );
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

    private String prepareRMsg( CRProduct product, int reqCode ) {
        JSONObject valParams = new JSONObject();
        JSONObject valProduct = new JSONObject();
        JSONArray valImages = new JSONArray();
        JSONArray valKeywords = new JSONArray();

        String strUserName = CRCliRoot.getInstance().mAccountData.mUserName;

        if ( strUserName.length() == 0 ) {
            assert( false );
            return "";
        }

        try {
            // username.
            valParams.put( "username", strUserName );
            // product.
            valParams.put( "product", valProduct );
            // title.
            valProduct.put( "title", product.mTitle );
            // price.
            valProduct.put( "price", product.mPrice );
            // describe.
            valProduct.put( "describe", product.mDescribe );
            // images.
            valProduct.put( "images", valImages );
            for ( String strImageUUID : product.mImages ) {
                valImages.put( strImageUUID );
            }
            // keywords.
            valProduct.put( "keywords", valKeywords );
            for ( String strKeyword : product.mKeywords )
            valKeywords.put( strKeyword );
            // request code.
            valParams.put( "request_code", reqCode );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_PRODUCT_PUBLISH );
    }

    private void addProduct2Pending( int reqCode, CRProduct product ) {
        synchronized ( mMapReqCode2ProductPending ) {
            if ( mMapReqCode2ProductPending.get( reqCode ) != null ) {
                assert( false );
            }
            mMapReqCode2ProductPending.put(reqCode, product);
        }

        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_PRODUCT_PENDQUEUE_ADD, reqCode, product );
    }

    private void delProductFromPending( int reqCode ) {
        synchronized ( mMapReqCode2ProductPending ) {
            if ( mMapReqCode2ProductPending.get( reqCode ) == null ) {
                return;
            }
            mMapReqCode2ProductPending.remove(reqCode);
        }

        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_PRODUCT_PENDQUEUE_REMOVE, reqCode, 0 );
    }

    private void moveProductPend2Finish( int reqCode ) {
        synchronized ( mMapReqCode2ProductPending ) {
            CRProduct product = mMapReqCode2ProductPending.get( reqCode );
            if ( product == null ) {
                return;
            }
            synchronized ( mContainerProduct ) {
                mContainerProduct.add( product );
            }
            mMapReqCode2ProductPending.remove( reqCode );
        }
    }

    private void onBtnClickCommitPublish() {
        FragmentPublish fragment = (FragmentPublish)CRCliRoot.getInstance().mUIDepot.getFragment( CRCliDef.CRCLI_FRAGMENT_PUBLISH );
        if ( fragment == null ) {
            return;
        }
        Activity activity = fragment.getActivity();
        if ( activity == null ) {
            return;
        }
        // maybe fill CRProduct code need move to FragmentPublish.
        CRProduct product = new CRProduct();
        int reqCode = allocateReqCode();
        // get title.
        TextView tvTitle = (TextView)activity.findViewById( R.id.et_publish_title );
        product.mTitle = tvTitle.getText().toString();
        // get price.
        TextView tvPrice = (TextView)activity.findViewById( R.id.et_publish_price );
        product.mPrice = tvPrice.getText().toString();
        // get describe.
        TextView tvDescribe = (TextView)activity.findViewById( R.id.et_publish_product_describe );
        product.mDescribe = tvDescribe.getText().toString();
        // get images.
        Set<UUID> key = mMapId2Uri.keySet();
        for ( Iterator it = key.iterator(); it.hasNext(); ) {
            UUID imageUUID = (UUID) it.next();
            String strImageUUID = imageUUID.toString();
            product.mImages.add( strImageUUID );
        }
        // get keywords.
        TextView tvKeywords = (TextView)activity.findViewById( R.id.et_publish_product_keyword );
        product.mKeywords.add( tvKeywords.getText().toString() );

        // add to mMapReqCode2ProductPending
        addProduct2Pending( reqCode, product );

        //
        String strRMsg = prepareRMsg( product, reqCode );
        byte[] rawBufRMsg = strRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );
    }

    private void onBtnClickScan4Publish() {

    }

    private void onBtnClickSelectPicture4Publish() {

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

    @Override
    public void accept(CRRMsgJson rmsg) {
        switch ( rmsg.mCmdType ) {
            case CRCliDef.CRCMDTYPE_ACK_PRODUCT_PUBLISH: {
                onRMsgProductPublish( rmsg );
            }
            break;
            default:
                break;
        }
    }

    private void onRMsgProductPublish( CRRMsgJson rmsg ) {
        boolean bSuccess = false;
        int nReqCode = 0;
        int nErrCode = 0;

        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject( "params" );
            bSuccess = valParams.getInt( "result" ) == 1;
            nReqCode = valParams.getInt( "request_code" );
            nErrCode = bSuccess ? 0 : valParams.getInt( "reason" );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ( bSuccess ) {
            moveProductPend2Finish(nReqCode);
        } else {
            delProductFromPending( nReqCode );

            Activity mainActivity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
            // show a notify dialog.
            new AlertDialog.Builder( mainActivity ).setTitle( "publish product result" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();
        }
    }
}

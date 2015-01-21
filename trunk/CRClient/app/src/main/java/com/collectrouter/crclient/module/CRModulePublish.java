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
import com.collectrouter.crclient.frame.CRRMsgHandlerDepot;
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
import java.util.Collection;
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
    private Map<UUID, CRProduct > mMapUUID2ProductPending;
    private List< CRProduct > mContainerProduct;
    private int mReqCodeBase = 0;

    public CRModulePublish( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        //
        mMapId2Uri = new Hashtable<>();
        mMapUUID2ProductPending = new Hashtable<>();
        mContainerProduct = new ArrayList<>();
        //
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_PHOTOGRAPH_4_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_SELECTPICTURE_4_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_SCAN_4_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_COMMIT_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_RECV_PHOTOGRAPH_4_PUBLISH, this );

        //
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_PRODUCT_PUBLISH, this );
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

    private String prepareRMsg( CRProduct product ) {
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
            // uuid of product.
            valProduct.put( "uuid", product.mUUID );
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
            for ( String strKeyword : product.mKeywords ) {
                valKeywords.put(strKeyword);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_PRODUCT_PUBLISH );
    }

    public CRProduct getProductPending( int nIndex ) {
        synchronized ( mMapUUID2ProductPending ) {
            if ( nIndex < 0 || nIndex >= mMapUUID2ProductPending.size() ) {
                return null;
            }
            Set< Map.Entry< UUID, CRProduct > > setProductPend = mMapUUID2ProductPending.entrySet();
            Iterator< Map.Entry< UUID, CRProduct > > itME = setProductPend.iterator();
            Map.Entry< UUID, CRProduct > meDest = null;
            for ( int i = 0; i<=nIndex; ++i ) {
                meDest = itME.next();
            }
            if ( meDest == null ) {
                assert( false );
                return null;
            }
            return meDest.getValue();
        }
    }

    public CRProduct getProductPublished( int nIndex ) {
        synchronized ( mContainerProduct ) {
            if ( nIndex < 0 || nIndex >= mContainerProduct.size() ) {
                assert( false );
                return null;
            }

            CRProduct product = mContainerProduct.get( nIndex );
            return product;
        }
    }

    public int getProductPendCount() {
        synchronized ( mMapUUID2ProductPending ) {
            return mMapUUID2ProductPending.size();
        }
    }

    public int getProductPublishedCount() {
        synchronized ( mContainerProduct ) {
            return mContainerProduct.size();
        }
    }

    private void addProduct2Pending( CRProduct product ) {
        synchronized ( mMapUUID2ProductPending ) {
            if ( mMapUUID2ProductPending.get( product.mUUID ) != null ) {
                assert( false );
            }
            mMapUUID2ProductPending.put(product.mUUID, product);
        }

        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_PRODUCT_PENDQUEUE_ADD, product.mUUID, product );
    }

    private void delProductFromPending( UUID uuid ) {
        synchronized ( mMapUUID2ProductPending ) {
            if ( mMapUUID2ProductPending.get( uuid ) == null ) {
                return;
            }
            mMapUUID2ProductPending.remove(uuid);
        }

        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_PRODUCT_PENDQUEUE_REMOVE, uuid, 0 );
    }

    private void moveProductPend2Finish( UUID uuid ) {
        CRProduct product = null;
        synchronized ( mMapUUID2ProductPending ) {
            product = mMapUUID2ProductPending.get( uuid );
            if ( product == null ) {
                return;
            }
            synchronized ( mContainerProduct ) {
                mContainerProduct.add( product );
            }
            mMapUUID2ProductPending.remove( uuid );
        }

        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_PRODUCT_PENDQUEUE_REMOVE, uuid, 0 );
        CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_PRODUCT_ADD, product, 0 );
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
        addProduct2Pending( product );

        //
        String strRMsg = prepareRMsg( product );
        byte[] rawBufRMsg = strRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );

        // switch UI 2 FragmentMyPublishList.
        ActivityMain activityMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        activityMain.switch2MyPublishList();

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
        String strUUID;
        UUID uuid;
        int nErrCode = 0;

        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject( "params" );
            bSuccess = valParams.getInt( "result" ) == 1;
            strUUID = valParams.getString( "product_uuid" );
            uuid = java.util.UUID.fromString( strUUID );
            nErrCode = bSuccess ? 0 : valParams.getInt( "reason" );
            //
            if ( bSuccess ) {
                moveProductPend2Finish( uuid );
            } else {
                delProductFromPending(uuid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ( !bSuccess ) {
            Activity mainActivity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
            // show a notify dialog.
            new AlertDialog.Builder( mainActivity ).setTitle( "publish product result" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();
        }
    }
}

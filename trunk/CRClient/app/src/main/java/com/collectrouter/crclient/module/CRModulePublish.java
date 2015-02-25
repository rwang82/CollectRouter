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
import com.collectrouter.crclient.ui.FragmentDoPublish;

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
    private Map<UUID, CRProduct > mMapUUID2ProductPending;
    private List< CRProduct > mContainerProduct;
    private int mReqCodeBase = 0;

    public CRModulePublish( CREventDepot eventDepot, CRRMsgHandlerDepot rmsgHandlerDepot ) {
        //
        mMapUUID2ProductPending = new Hashtable<>();
        mContainerProduct = new ArrayList<>();
        //
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH, this );
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_COMMIT_PUBLISH, this );

        //
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_PRODUCT_PUBLISH, this );
    }

    private synchronized int allocateReqCode() {
        return mReqCodeBase++;
    }

    private void onBtnClickEnterPublish() {
        ActivityMain activityMain = (ActivityMain)CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );

        if ( activityMain == null ) {
            return;
        }

        //
        activityMain.switch2DoPublish();
        activityMain.closeDrawer();

    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_BTNCLICK_ENTER_PUBLISH:{
                onBtnClickEnterPublish();
            }
            break;
            case CRCliDef.CREVT_BTNCLICK_COMMIT_PUBLISH: {
                onBtnClickCommitPublish( (CRProduct)param1 );
            }
            break;
            default:
            break;
        }
    }

    private String prepareRMsg( CRProduct product ) {
        JSONObject valParams = new JSONObject();
        JSONObject valProduct = new JSONObject();
        JSONArray valImages = new JSONArray();
        JSONArray valKeywords = new JSONArray();

        if ( !CRCliRoot.getInstance().mModuleLogin.islogined() ) {
            assert( false );
            return "";
        }

        try {
            // username.
            valParams.put( "username", CRCliRoot.getInstance().mData.mCurLoginAccountName );
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
            // default type.
            valProduct.put( "sort", product.mSort );
            // user define type.
            if ( product.mSort == -1 ) {
                valProduct.put( "udsort", product.mUDSort );
            }
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

    private void onBtnClickCommitPublish( CRProduct product ) {

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
        String strAccountName;
        UUID uuid;
        int nErrCode = 0;

        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject( "params" );
            bSuccess = valParams.getInt( "result" ) == 1;
            strUUID = valParams.getString( "product_uuid" );
            strAccountName = valParams.getString( "username" );
            uuid = java.util.UUID.fromString( strUUID );
            nErrCode = bSuccess ? 0 : valParams.getInt( "reason" );
            //
            if ( bSuccess ) {
                //
                CRCliRoot.getInstance().mData.doFetchAccountData( strAccountName );
                //
                if ( !strAccountName.equals( CRCliRoot.getInstance().mData.mCurLoginAccountName ) )
                    return;
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

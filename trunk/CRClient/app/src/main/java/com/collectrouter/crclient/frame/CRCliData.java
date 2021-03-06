package com.collectrouter.crclient.frame;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;

import com.collectrouter.common.CRUtil;
import com.collectrouter.crclient.data.CRProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rom on 2/4 0004.
 */
public class CRCliData implements CRRMsgJsonHandlerBase{
    public String mCurLoginAccountName;
    public Map<String, CRAccountData> mMapUserName2AccountData;
    public Map<String, List<String>> mMapUserName2Attetioned;
    public Map<String, List<String>> mMapUserName2Attetion;
    public Map<String, List<String>> mMapUserName2Published;
    public Map<String, CRProduct> mMapUUID2Product;

    public CRCliData( CRRMsgHandlerDepot rmsgHandlerDepot ) {
        mCurLoginAccountName = new String("");
        mMapUserName2AccountData = new Hashtable<>();
        mMapUserName2Attetioned = new Hashtable<>();
        mMapUserName2Attetion = new Hashtable<>();
        mMapUserName2Published = new Hashtable<>();
        mMapUUID2Product = new Hashtable<>();
        //
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_FETCH_ACCOUNTINFO, this );
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_FETCH_ATTETION_LIST, this );
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_FETCH_ACCOUNTPRODUCTS, this );
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_FETCH_ATTETIONED_LIST, this );
    }

    public void setCurAccountData( CRAccountData accountData ) {
        if ( accountData == null ) {
            mCurLoginAccountName = "";
            return;
        }
        mCurLoginAccountName = accountData.mUserName;
        saveAccountData( accountData );
    }

    public CRAccountData getCurAccountData() {
        synchronized ( mMapUserName2AccountData ) {
            if (mCurLoginAccountName.length() == 0)
                return null;

            return mMapUserName2AccountData.get(mCurLoginAccountName);
        }
    }

    static public class FAPItem {
        public FAPItem( String strAccountName, int nIndexStart, int nCount ) {
            mAccountName = strAccountName;
            mIndexStart = nIndexStart;
            mCount = nCount;
        }
        String mAccountName;
        int mIndexStart;
        int mCount;
    }

//
    public List< String > getAttetionList( String strAccountName ) {
        synchronized ( mMapUserName2Attetion ) {
            List<String> attetionListRet = mMapUserName2Attetion.get(strAccountName);
            if ( attetionListRet == null )
                return new ArrayList<>();
            else
                return attetionListRet;
        }
/////////////////////////////////////////////////////////////////
//        List<String> attetionListRet = new ArrayList<>();
//        synchronized ( mMapUserName2Attetion ) {
//            List<String> dest = mMapUserName2Attetion.get(strAccountName);
//            if (dest == null)
//                return attetionListRet;
//            for (String item : dest) {
//                attetionListRet.add(item);
//            }
//        }
//        return attetionListRet;
/////////////////////////////////////////////////////////////////
    }

    public List< CRProduct > getAccountProducts( String strAccountName ) {
        List< CRProduct > listProducts = new ArrayList<>();
        List< String > listUUID = mMapUserName2Published.get( strAccountName );

        for ( String strUUID : listUUID ) {
            CRProduct item = mMapUUID2Product.get( strUUID );
            if ( item != null ) {
                listProducts.add( item );
            }
        }

        return listProducts;
    }

    public List< CRProduct > getAccountProducts( List< String > listAccountName ) {
        List< CRProduct > listProducts = new ArrayList<>();

        for ( String accountName : listAccountName ) {
            List< String > listUUID = mMapUserName2Published.get( accountName );
            if ( listUUID == null )
                continue;
            for ( String strUUID : listUUID ) {
                CRProduct item = mMapUUID2Product.get( strUUID );
                if ( item != null ) {
                    listProducts.add( item );
                }
            }
        }

        return listProducts;
    }

    public CRAccountData getAccountData( String accountName ) {
        return mMapUserName2AccountData.get( accountName );
    }

    public List< CRAccountData > getAccountDataList( List< String > listAccountName ) {
        List< CRAccountData > listAccountData = new ArrayList<>();

        for ( String strAccountName : listAccountName ) {
            CRAccountData accountData = mMapUserName2AccountData.get( strAccountName );
            listAccountData.add( accountData );
        }

        return listAccountData;
    }

    public int doFetchAccountProducts( FAPItem fapParam ) {
        List< FAPItem > listFAP = new ArrayList< FAPItem >();
        listFAP.add( fapParam );

        return doFetchAccountProducts( listFAP );
    }

//
    public int doFetchAccountProducts( List< FAPItem > listFAP ) {
        if ( listFAP.size() == 0 )
            return CRCliDef.CRCMDSN_INVALID;
        CRRMsgMaker.CRRMsgReq rmsgReq = prepareFetchAccountProducts( listFAP );
        if ( rmsgReq == null )
            return CRCliDef.CRCMDSN_INVALID;
        byte[] rawBufRMsg = rmsgReq.mRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );
        return rmsgReq.mSN;
    }

    public int doFetchAttetionRecord( String strAccountName, int nIndexStart, int nCount ) {
        if ( strAccountName.length() == 0 )
            return CRCliDef.CRCMDSN_INVALID;
        CRRMsgMaker.CRRMsgReq rmsgReq = prepareFetchAttetionRecord( strAccountName, nIndexStart, nCount );
        if ( rmsgReq == null )
            return CRCliDef.CRCMDSN_INVALID;
        byte[] rawBufRMsg = rmsgReq.mRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );
        return rmsgReq.mSN;
    }
    public int doFetchAttetionedRecord( String strAccountName, int nIndexStart, int nCount ) {
        if ( strAccountName.length() == 0 )
            return CRCliDef.CRCMDSN_INVALID;
        CRRMsgMaker.CRRMsgReq rmsgReq = prepareFetchAttetionedRecord( strAccountName, nIndexStart, nCount );
        if ( rmsgReq == null )
            return CRCliDef.CRCMDSN_INVALID;
        byte[] rawBufRMsg = rmsgReq.mRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );
        return rmsgReq.mSN;
    }

    public int doFetchAccountData( String strAccountName ) {
        if ( strAccountName.length() == 0 )
            return CRCliDef.CRCMDSN_INVALID;
        List<String> listUserName = new ArrayList<>();
        listUserName.add( strAccountName );
        return doFetchAccountData( listUserName );
    }

    public int doFetchAccountData( List<String> listUserName ) {
        CRRMsgMaker.CRRMsgReq rmsgReq = prepareFetchAccountInfoRMsg( listUserName );
        if ( rmsgReq == null )
            return CRCliDef.CRCMDSN_INVALID;
        byte[] rawBufRMsg = rmsgReq.mRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );
        return rmsgReq.mSN;
    }

//
    private CRRMsgMaker.CRRMsgReq prepareFetchAccountProducts( List< FAPItem > listFAP ) {
        JSONObject valParams = new JSONObject();
        JSONArray valUserList = new JSONArray();

        try {
            for ( FAPItem item : listFAP ) {
                valUserList.put( item.mAccountName );
            }
            valParams.put("username_list", valUserList);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_FETCH_ACCOUNTPRODUCTS );
    }

    private CRRMsgMaker.CRRMsgReq prepareFetchAttetionedRecord( String strAccountName, int nIndexStart, int nCount ) {
        if ( nIndexStart < 0 || nCount <= 0 )
            return null;
        JSONObject valParams = new JSONObject();

        try {
            valParams.put( "username", strAccountName );
            valParams.put( "index_start", nIndexStart );
            valParams.put( "count", nCount );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_FETCH_ATTETIONED_LIST );
    }

    private CRRMsgMaker.CRRMsgReq prepareFetchAttetionRecord( String strAccountName, int nIndexStart, int nCount ) {
        if ( nIndexStart < 0 || nCount <= 0 )
            return null;
        JSONObject valParams = new JSONObject();

        try {
            valParams.put( "username", strAccountName );
            valParams.put( "index_start", nIndexStart );
            valParams.put( "count", nCount );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_FETCH_ATTETION_LIST );
    }

    private CRRMsgMaker.CRRMsgReq prepareFetchAccountInfoRMsg( List<String> listUserName ) {
        JSONObject valParams = new JSONObject();
        JSONArray valUserNames = new JSONArray();

        try {
            for ( String strUserName : listUserName ) {
                valUserNames.put( strUserName );
            }
            valParams.put( "username_list", valUserNames );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return CRRMsgMaker.createRMsg( valParams, CRCliDef.CRCMDTYPE_REQ_FETCH_ACCOUNTINFO );
    }

    @Override
    public void accept(CRRMsgJson rmsg) {
        switch ( rmsg.mCmdType ) {
            case CRCliDef.CRCMDTYPE_ACK_FETCH_ACCOUNTINFO:
            {
                onRMsgAckFetchAccountInfo(rmsg);
            }
            break;
            case CRCliDef.CRCMDTYPE_ACK_FETCH_ATTETION_LIST:
            {
                onRMsgAckFetchAttetionList(rmsg);
            }
            break;
            case CRCliDef.CRCMDTYPE_ACK_FETCH_ACCOUNTPRODUCTS:
            {
                onRMsgAckFetchAccountProducts( rmsg );
            }
            break;
            case CRCliDef.CRCMDTYPE_ACK_FETCH_ATTETIONED_LIST:
            {
                onRMsgAckFetchAttetionedList(rmsg);
            }
            break;
            default:
                break;
        }
    }

    private void onRMsgAckFetchAccountProducts(CRRMsgJson rmsg) {
        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject( "params" );
            boolean bSuccess = valParams.getInt( "result" ) == 1;
            if ( !bSuccess ) {
                int nErrCode = valParams.getInt( "reason" );
                Activity mainActivity = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
                // show a notify dialog.
                new AlertDialog.Builder( mainActivity ).setTitle( "login result" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();
                return;
            } else {
                List<String> listAccountName = new ArrayList<>();
                JSONObject valAccountProductList = valParams.getJSONObject("account_products_list");
                Iterator<String> itAccountName = valAccountProductList.keys();
                while ( itAccountName.hasNext() ) {
                    String accountName = itAccountName.next().toString();
                    listAccountName.add( accountName );
                    JSONArray aryPL = valAccountProductList.getJSONArray( accountName );
                    List<CRProduct> listProduct = new ArrayList<>();

                    if ( !parseProductList( aryPL, listProduct ) )
                        continue;

                    for ( CRProduct product : listProduct ) {
                        saveProduct( product );
                    }
                }

                //
                CRCliRoot.getInstance().mEventDepot.fire(CRCliDef.CREVT_FETCH_ACCOUNT_PRODUCTS_SUCCESS, rmsg.mSN, listAccountName );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class CRAttetionListResult {
        CRAttetionListResult( String accountName, List<String> listAttetionName ) {
            mAccountName = accountName;
            mListAttetionName = listAttetionName;
        }
        public String mAccountName;
        public List< String > mListAttetionName;
    }

    public static class CRAttetionedListResult {
        CRAttetionedListResult( String accountName, List<String> listAttetionedName ) {
            mAccountName = accountName;
            mListAttetionedName = listAttetionedName;
        }
        public String mAccountName;
        public List< String > mListAttetionedName;
    }

    private void onRMsgAckFetchAttetionedList(CRRMsgJson rmsg) {
        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject( "params" );
            boolean bSuccess = valParams.getInt( "result" ) == 1;
            if ( !bSuccess ) {
                int nErrCode = valParams.getInt( "reason" );
                Activity mainActivity = CRCliRoot.getInstance().mUIDepot.getActivity(CRCliDef.CRCLI_ACTIVITY_MAIN);
                // show a notify dialog.
                new AlertDialog.Builder( mainActivity ).setTitle( "fetch attetioned list" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();
            } else {
                String strAccountName = valParams.getString("username");
                JSONArray valAttetionedList = valParams.getJSONArray( "attetioneds" );
                List<String> listAccountedName = new ArrayList<>();
                int nSize = valAttetionedList.length();
                for ( int nIndex = 0; nIndex<nSize; ++nIndex ) {
                    String item = valAttetionedList.getString( nIndex );
                    listAccountedName.add(item);
                }
                //
                setAttetionedList( strAccountName, listAccountedName );
                //
                CRAttetionedListResult result = new CRAttetionedListResult( strAccountName, listAccountedName );
                CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_FETCH_ATTETIONED_LIST_SUCCESS, rmsg.mSN, result );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onRMsgAckFetchAttetionList(CRRMsgJson rmsg) {
        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject( "params" );
            boolean bSuccess = valParams.getInt( "result" ) == 1;
            if ( !bSuccess ) {
                int nErrCode = valParams.getInt( "reason" );
                Activity mainActivity = CRCliRoot.getInstance().mUIDepot.getActivity(CRCliDef.CRCLI_ACTIVITY_MAIN);
                // show a notify dialog.
                new AlertDialog.Builder( mainActivity ).setTitle( "login result" ).setMessage( bSuccess ? "succeed" : "failed, ERRCODE:" + nErrCode ).setPositiveButton( "OK", null ).show();
            } else {
                String strAccountName = valParams.getString("username");
                JSONArray valAttetionList = valParams.getJSONArray( "attetions" );
                List<String> listAccountName = new ArrayList<>();
                int nSize = valAttetionList.length();
                for ( int nIndex = 0; nIndex<nSize; ++nIndex ) {
                    String item = valAttetionList.getString( nIndex );
                    listAccountName.add(item);
                }
                //
                setAttetionList( strAccountName, listAccountName );
                //
                CRAttetionListResult result = new CRAttetionListResult( strAccountName, listAccountName );
                CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_FETCH_ATTETION_LIST_SUCCESS, rmsg.mSN, result );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onRMsgAckFetchAccountInfo( CRRMsgJson rmsg ) {
        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject("params");
            boolean bSuccess = ( valParams.getInt( "result" ) == 1 );
            if ( bSuccess ) {
                List< CRAccountData > listAccountData = new ArrayList<>();
                JSONArray accountList = valParams.getJSONArray( "account_info_list" );
                int nSizeAccount = accountList.length();
                for ( int nIndex = 0; nIndex<nSizeAccount; ++nIndex ) {
                    JSONObject valAccountData = accountList.getJSONObject( nIndex );
                    CRAccountData accountData = parseAccountData( valAccountData );
                    if ( accountData != null ) {
                        listAccountData.add( accountData );
                        saveAccountData( accountData );
                    }
                }
                //
                CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_FETCH_ACCOUNT_DATA_SUCCESS, rmsg.mSN, listAccountData );
            } else {
                int nRes = valParams.getInt("reason");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void saveProduct( CRProduct product ) {
        //
        mMapUUID2Product.put( product.mUUID.toString(), product );
        //
        List< String > listUUID = mMapUserName2Published.get( product.mPublisher );
        if ( listUUID == null ) {
            listUUID = new ArrayList<>();
            mMapUserName2Published.put( product.mPublisher, listUUID );
        }
        if ( !CRUtil.IsExist( listUUID, product.mUUID.toString() ) ) {
            listUUID.add( product.mUUID.toString() );
        }
    }

    boolean parseProductList( JSONArray aryPL, List<CRProduct> listProduct ) {
        int nSizeProduct = aryPL.length();
        for ( int nIndex = 0; nIndex<nSizeProduct; ++nIndex ) {
            try {
                JSONObject valProduct = (JSONObject) aryPL.get( nIndex );
                CRProduct product = parseProduct( valProduct );
                if ( product == null )
                    return false;
                listProduct.add( product );
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return true;
    }

    CRProduct parseProduct( JSONObject valProduct ) {
        CRProduct product = new CRProduct();
        try {
            product.mUUID = java.util.UUID.fromString( valProduct.getString( "uuid" ) );
            product.mPublisher = valProduct.getString( "publisher" );
            product.mTitle = valProduct.getString( "title" );
            product.mPrice = valProduct.getString( "price" );
            product.mDescribe = valProduct.getString( "describe" );
            product.mSort = valProduct.getInt( "sort" );
            product.mUDSort = valProduct.getString( "udsort" );

            JSONArray aryImages = valProduct.getJSONArray( "images" );
            if ( !parseStringList( aryImages, product.mImages ) )
                return null;
            JSONArray aryKeyword = valProduct.getJSONArray( "keywords" );
            if ( !parseStringList( aryKeyword, product.mKeywords ) )
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


        return product;
    }

    boolean parseStringList( JSONArray aryStr, List< String > listStr ) {
        int nSize = aryStr.length();
        try {
            for ( int nIndex = 0; nIndex<nSize; ++nIndex ) {
                String strItem = aryStr.getString(nIndex);
                listStr.add( strItem );
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void setAttetionList( String strAccountName, List<String> listAccountName ) {
        synchronized ( mMapUserName2Attetion ) {
            mMapUserName2Attetion.put( strAccountName, listAccountName );
        }
    }

    private void setAttetionedList( String strAccountName, List<String> listAccountName ) {
        synchronized ( mMapUserName2Attetioned ) {
            mMapUserName2Attetioned.put( strAccountName, listAccountName );
        }
    }

    private CRAccountData parseAccountData( JSONObject valAccountData ) {
        CRAccountData accountData = new CRAccountData();
        try {
            accountData.mUserName = valAccountData.getString( "username" );
            accountData.mPhone = valAccountData.getString( "phone" );
            accountData.mEMail = valAccountData.getString( "email" );
            accountData.mNickName = valAccountData.getString( "nickname" );
            accountData.mSortType = valAccountData.getInt("sort");
            accountData.mCountAttetioned = valAccountData.getInt( "attetioned" );
            accountData.mCountAttetion = valAccountData.getInt( "attetion" );
            accountData.mCountPublished = valAccountData.getInt( "published" );

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return accountData;
    }

    private void saveAccountData( CRAccountData accountData ) {
        //
        synchronized ( mMapUserName2AccountData ) {
            mMapUserName2AccountData.put( accountData.mUserName, accountData );
        }

        // check if need update cur login account info.
        if ( mCurLoginAccountName.equals( accountData.mUserName ) ) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_CUR_LOGIN_ACCOUNT_INFO_UPDATE, 0, 0 );
        }

    }

}

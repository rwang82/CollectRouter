package com.collectrouter.crclient.frame;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
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

    public CRCliData( CRRMsgHandlerDepot rmsgHandlerDepot ) {
        mCurLoginAccountName = new String("");
        mMapUserName2AccountData = new Hashtable<>();
        mMapUserName2Attetioned = new Hashtable<>();
        mMapUserName2Attetion = new Hashtable<>();
        mMapUserName2Published = new Hashtable<>();
        //
        rmsgHandlerDepot.regRMsgHandler( CRCliDef.CRCMDTYPE_ACK_FETCH_ACCOUNTINFO, this );
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
        if ( mCurLoginAccountName.length() == 0 )
            return null;

        return mMapUserName2AccountData.get( mCurLoginAccountName );
    }

    public void refetchCurAccountData() {
        if ( mCurLoginAccountName.length() == 0 )
            return;
        List<String> listUserName = new ArrayList<>();
        listUserName.add( mCurLoginAccountName );
        refetchAccountData(listUserName);
    }

    public void refetchAccountData( List<String> listUserName ) {
        String strRMsg = prepareFetchAccountInfoRMsg( listUserName );
        byte[] rawBufRMsg = strRMsg.getBytes();
        CRCliRoot.getInstance().mNWPClient.sendData( rawBufRMsg, rawBufRMsg.length );
    }

    private String prepareFetchAccountInfoRMsg( List<String> listUserName ) {
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
                onRMsgFetchAccountInfo( rmsg );
            }
            break;
            default:
                break;
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
        mMapUserName2AccountData.put( accountData.mUserName, accountData );

        // check if need update cur login account info.
        if ( mCurLoginAccountName.equals( accountData.mUserName ) ) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_CUR_LOGIN_ACCOUNT_INFO_UPDATE, 0, 0 );
        }

    }

    private void onRMsgFetchAccountInfo( CRRMsgJson rmsg ) {
        try {
            JSONObject valParams = rmsg.mJsonRoot.getJSONObject("params");
            boolean bSuccess = ( valParams.getInt( "result" ) == 1 );
            if ( bSuccess ) {
                JSONArray accountList = valParams.getJSONArray( "account_info_list" );
                int nSizeAccount = accountList.length();
                for ( int nIndex = 0; nIndex<nSizeAccount; ++nIndex ) {
                    JSONObject valAccountData = accountList.getJSONObject( nIndex );
                    CRAccountData accountData = parseAccountData( valAccountData );
                    if ( accountData != null ) {
                        saveAccountData( accountData );
                    }
                }
            } else {
                int nRes = valParams.getInt("reason");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

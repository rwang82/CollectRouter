package com.collectrouter.crclient.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collectrouter.crclient.adapter.FSAccountsAdapterDefault;
import com.collectrouter.crclient.data.CRProduct;
import com.collectrouter.crclient.frame.CRAccountData;
import com.collectrouter.crclient.frame.CRCliData;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 2/13 0013.
 */
public class FragmentShowAttetioned extends FragmentShowAccounts implements CREventHandler {
    private enum ENUMSTATE{
        ES_INIT,
        ES_FETCHING_ATTETIONED_LIST,
        ES_FETCHING_ACCOUNT_DATA,
        ES_FETCHING_PRODUCTS,
        ES_ACCOMPLISH }

    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_SHOW_ATTETIONED;
    private ENUMSTATE mEState = ENUMSTATE.ES_INIT;
    private int mReqSN = CRCliDef.CRCMDSN_INVALID;

    public FragmentShowAttetioned() {

    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        //
        CRCliRoot.getInstance().mEventDepot.regEventHandler( CRCliDef.CREVT_FETCH_ATTETION_LIST_SUCCESS, this );
        CRCliRoot.getInstance().mEventDepot.regEventHandler( CRCliDef.CREVT_FETCH_ACCOUNT_DATA_SUCCESS, this );
        CRCliRoot.getInstance().mEventDepot.regEventHandler( CRCliDef.CREVT_FETCH_ACCOUNT_PRODUCTS_SUCCESS, this );
        CRCliRoot.getInstance().mEventDepot.regEventHandler( CRCliDef.CREVT_FETCH_ATTETIONED_LIST_SUCCESS, this );

        //
        if ( mEState == ENUMSTATE.ES_INIT ) {
            mEState = ENUMSTATE.ES_FETCHING_ATTETIONED_LIST;
            CRAccountData curAccountData = CRCliRoot.getInstance().mData.getCurAccountData();
            if ( curAccountData != null ) {
                mReqSN = CRCliRoot.getInstance().mData.doFetchAttetionedRecord( curAccountData.mUserName, 0, curAccountData.mCountAttetion );
            }
        }
        //
        return super.onCreateView( inflater, container, savedInstanceState );
    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mEventDepot.unRegEventHandler( CRCliDef.CREVT_FETCH_ATTETION_LIST_SUCCESS, this );
        CRCliRoot.getInstance().mEventDepot.unRegEventHandler( CRCliDef.CREVT_FETCH_ACCOUNT_DATA_SUCCESS, this );
        CRCliRoot.getInstance().mEventDepot.unRegEventHandler( CRCliDef.CREVT_FETCH_ACCOUNT_PRODUCTS_SUCCESS, this );
        CRCliRoot.getInstance().mEventDepot.unRegEventHandler( CRCliDef.CREVT_FETCH_ATTETIONED_LIST_SUCCESS, this );
        //
        super.onDestroyView();
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_FETCH_ATTETIONED_LIST_SUCCESS:
            {
                int nSN = (int)param1;
                CRCliData.CRAttetionedListResult result = (CRCliData.CRAttetionedListResult)param2;

                if ( nSN != mReqSN )
                    return;
                if ( mEState != ENUMSTATE.ES_FETCHING_ATTETIONED_LIST )
                    return;
                mEState = ENUMSTATE.ES_FETCHING_ACCOUNT_DATA;
                mReqSN = CRCliRoot.getInstance().mData.doFetchAccountData( result.mListAttetionedName );
            }
            break;
            case CRCliDef.CREVT_FETCH_ACCOUNT_DATA_SUCCESS:
            {
                int nSN = (int)param1;
                List<CRAccountData> listAccountData = ( List<CRAccountData> )param2;

                if ( nSN != mReqSN )
                    return;
                if ( mEState != ENUMSTATE.ES_FETCHING_ACCOUNT_DATA )
                    return;
                mEState = ENUMSTATE.ES_FETCHING_PRODUCTS;

                List<CRCliData.FAPItem> listFAP = new ArrayList<>();
                for ( CRAccountData accountData : listAccountData ) {
                    CRCliData.FAPItem fapItem = new CRCliData.FAPItem( accountData.mUserName, 0, accountData.mCountAttetion );
                    listFAP.add( fapItem );
                }
                mReqSN = CRCliRoot.getInstance().mData.doFetchAccountProducts( listFAP );
            }
            break;
            case CRCliDef.CREVT_FETCH_ACCOUNT_PRODUCTS_SUCCESS:
            {
                int nSN = (int)param1;
                List< String > listAccountName = ( List< String > )param2;

                if ( nSN != mReqSN )
                    return;

                List< CRAccountData > listAccountData = CRCliRoot.getInstance().mData.getAccountDataList( listAccountName );
                List<CRProduct> listProducts = CRCliRoot.getInstance().mData.getAccountProducts( listAccountName );

                FSAccountsAdapterDefault adapterFSA = new FSAccountsAdapterDefault( listAccountData, listProducts );
                setShowAccountsAdapter( adapterFSA );
                setShowProductsAdapter( adapterFSA );
            }
            break;
            default:
                break;
        }
    }
}

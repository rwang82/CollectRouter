package com.collectrouter.crclient.adapter;

import com.collectrouter.crclient.frame.CRAccountData;

import java.util.List;

/**
 * Created by rom on 2/14 0014.
 */
public interface CRShowAccountsAdapterBase {
    public int getAccountCount();
    public List<CRAccountData> getAccountsData();
    public CRAccountData getAccountData( int index );
}

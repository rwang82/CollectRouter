package com.collectrouter.crclient.adapter;

import com.collectrouter.crclient.data.CRProduct;

import java.util.List;

/**
 * Created by rom on 2/14 0014.
 */
public interface CRShowProductsAdapterBase {
    public int getProductSortCount();
    public String getProductSortName( int index );
    public List<CRProduct> getProductsBySort( int nSortIndex );
    public List<CRProduct> getProducts();
    public List<CRProduct> getProducts( int nFetchCount );
}

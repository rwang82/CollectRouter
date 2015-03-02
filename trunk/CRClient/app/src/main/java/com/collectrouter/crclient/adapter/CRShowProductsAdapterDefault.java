package com.collectrouter.crclient.adapter;

import com.collectrouter.crclient.data.CRProduct;
import com.collectrouter.crclient.frame.CRMisc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rom on 2/28 0028.
 */
public class CRShowProductsAdapterDefault implements CRShowProductsAdapterBase
{
    private List< CRProduct > mListProducts;
    private Map< Integer, List< CRProduct > > mMapPS2PL;
    private Map< String, List< CRProduct > > mMapPSUD2PL;

    public CRShowProductsAdapterDefault( List<CRProduct> listProducts ) {
        mListProducts = listProducts;
        mMapPS2PL = new Hashtable<>();
        mMapPSUD2PL = new Hashtable<>();

        //
        parseRawData();
    }

    private void parseRawData() {
        for ( CRProduct product : mListProducts ) {
            if ( product.mSort == -1 ) {
                List<CRProduct> listProduct = getProductList( product.mUDSort );
                if ( listProduct == null ) {
                    listProduct = new ArrayList< CRProduct>();
                    mMapPSUD2PL.put( product.mUDSort, listProduct );
                }
                listProduct.add( product );
            } else {
                List<CRProduct> listProduct = getProductList( product.mSort );
                if ( listProduct == null ) {
                    listProduct = new ArrayList< CRProduct >();
                    mMapPS2PL.put( product.mSort, listProduct );
                }
                listProduct.add( product );
            }
        }
    }

    private List< CRProduct > getProductList( int nSortType ) {
        return mMapPS2PL.get( nSortType );
    }

    private List< CRProduct > getProductList( String strSortType ) {
        return mMapPSUD2PL.get(strSortType);
    }

    @Override
    public int getProductSortCount() {
        return mMapPS2PL.size() + mMapPSUD2PL.size();
    }

    @Override
    public String getProductSortName(int index) {
        if ( index < 0 ) {
            return "";
        } else if ( index < mMapPS2PL.size() ) {
            Iterator<Integer> itPS = mMapPS2PL.keySet().iterator();
            int nIndexTmp = 0;
            while ( itPS.hasNext() ) {
                int nSortType = itPS.next();
                if ( nIndexTmp == index ) {
                    return CRMisc.getProductSort(nSortType);
                }
                ++nIndexTmp;
            }
            assert( false );
            return "";
        } else if ( index < mMapPS2PL.size() + mMapPSUD2PL.size() ) {
            int nIndexModify = index - mMapPS2PL.size();
            Iterator< String > itPSUD = mMapPSUD2PL.keySet().iterator();
            int nIndexTmp = 0;
            while ( itPSUD.hasNext() ) {
                String strSortType = itPSUD.next();
                if ( nIndexTmp == nIndexModify ) {
                    return strSortType;
                }
                ++nIndexTmp;
            }
            assert( false );
            return "";
        }
        return "";
    }

    @Override
    public List<CRProduct> getProductsBySort(int nSortIndex) {

        List<CRProduct> listProducts = new ArrayList<CRProduct>();
        if ( nSortIndex < 0 ) {
            return listProducts;
        } else if ( nSortIndex < mMapPS2PL.size() ) {
            Iterator<Integer> itPS = mMapPS2PL.keySet().iterator();
            int nIndexTmp = 0;
            while ( itPS.hasNext() ) {
                int nSortType = itPS.next();
                if ( nIndexTmp == nSortIndex ) {
                    return mMapPS2PL.get( nSortIndex );
                }
                ++nIndexTmp;
            }
            assert( false );
        } else if ( nSortIndex < mMapPS2PL.size() + mMapPSUD2PL.size() ) {
            int nIndexModify = nSortIndex - mMapPS2PL.size();
            Iterator< String > itPSUD = mMapPSUD2PL.keySet().iterator();
            int nIndexTmp = 0;
            while ( itPSUD.hasNext() ) {
                String strSortType = itPSUD.next();
                if ( nIndexTmp == nIndexModify ) {
                    return mMapPSUD2PL.get( strSortType );
                }
                ++nIndexTmp;
            }
            assert( false );
        } else {
            assert( false );
        }

        return listProducts;
    }

    @Override
    public List<CRProduct> getProducts() {
        return mListProducts;
    }

    @Override
    public List<CRProduct> getProducts(int nFetchCount) {

        int nSum = 0;
        List<CRProduct> listProductRet = new ArrayList<CRProduct>();
        for ( CRProduct product : mListProducts ) {
            listProductRet.add( product );
            ++nSum;
            if ( nSum == nFetchCount ) {
                return listProductRet;
            }
        }
        return listProductRet;
    }
}

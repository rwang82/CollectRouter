package com.collectrouter.crclient.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 1/16 0016.
 */
public class CRProduct {
    public String mTitle;
    public String mPrice;
    public String mDescribe;
    public List< String > mImages;
    public List< String > mKeywords;

    public CRProduct() {
        mTitle = "";
        mPrice = "";
        mDescribe = "";
        mImages = new ArrayList<>();
        mKeywords = new ArrayList<>();
    }
}

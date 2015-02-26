package com.collectrouter.crclient.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rom on 1/16 0016.
 */
public class CRProduct {
    public UUID mUUID;
    public String mTitle;
    public String mPrice;
    public String mDescribe;
    public String mPublisher;
    public int mSort; //mSort = -1, means mUDSort is useful.
    public String mUDSort;
    public List< String > mImages;
    public List< String > mKeywords;

    public CRProduct() {
        mUUID = java.util.UUID.randomUUID();
        mTitle = "";
        mPrice = "";
        mDescribe = "";
        mPublisher = "";
        mSort = -1;
        mUDSort = "UnDefine";
        mImages = new ArrayList<>();
        mKeywords = new ArrayList<>();

    }
}

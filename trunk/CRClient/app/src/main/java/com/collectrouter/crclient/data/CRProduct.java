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
    public String mUserName;
    public int mType; //mType = -1, means mUserDefineType is useful.
    public String mUserDefineType;
    public List< String > mImages;
    public List< String > mKeywords;

    public CRProduct() {
        mUUID = java.util.UUID.randomUUID();
        mTitle = "";
        mPrice = "";
        mDescribe = "";
        mUserName = "";
        mType = -1;
        mUserDefineType = "UnDefine";
        mImages = new ArrayList<>();
        mKeywords = new ArrayList<>();

    }
}

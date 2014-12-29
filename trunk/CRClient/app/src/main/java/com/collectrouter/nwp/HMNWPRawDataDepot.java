package com.collectrouter.nwp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 12/26 0026.
 */
public class HMNWPRawDataDepot {
    List< HMNWPRawData > mListRawData;

    public HMNWPRawDataDepot () {
        mListRawData = new ArrayList< HMNWPRawData>();
    }

    boolean add( byte[] rawBuf, int nLenRawBuf ) {
        if ( rawBuf.length < nLenRawBuf )
            return false;
        mListRawData.add( new HMNWPRawData( rawBuf, nLenRawBuf ) );
        return true;
    }

    int getCount() {
        return mListRawData.size();
    }
}

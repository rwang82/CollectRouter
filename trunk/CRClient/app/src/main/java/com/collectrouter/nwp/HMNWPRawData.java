package com.collectrouter.nwp;

/**
 * Created by rom on 12/26 0026.
 */
public class HMNWPRawData {
    byte[] mData;
    int mLen;

    HMNWPRawData( byte[] rawData, int nLen ) {
        assert( nLen > 0 && nLen <= rawData.length );
        mData = new byte[ nLen ];
        System.arraycopy( rawData, 0, mData, 0, nLen );
        mLen = nLen;
    }
}

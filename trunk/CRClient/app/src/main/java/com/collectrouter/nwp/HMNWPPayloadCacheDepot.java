package com.collectrouter.nwp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rom on 2/28 0028.
 */
public class HMNWPPayloadCacheDepot {
    private List< byte[] > mListPayload = new ArrayList<>();

    public void addItem( byte[] payload ) {
        mListPayload.add( payload );
    }

    public int getCount() { return mListPayload.size(); }

    public void clearAll() {
        mListPayload.clear();
    }

    public byte[] restoreRawPayload(){
        int nLenRaw = getRawPayloadLen();
        byte[] rawPayload = new byte[ nLenRaw ];
        int nPosStart = 0;
        for ( byte[] item : mListPayload ) {
            if ( nPosStart + item.length > nLenRaw ) {
                assert( false );
                break;
            }
            System.arraycopy( item, 0, rawPayload, nPosStart, item.length );
            nPosStart += item.length;
        }
        return rawPayload;
    }

    private int getRawPayloadLen() {
        int nLen = 0;
        for ( byte[] item : mListPayload ) {
            nLen += item.length;
        }
        return nLen;
    }
}

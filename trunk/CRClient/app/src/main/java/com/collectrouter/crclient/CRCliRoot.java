package com.collectrouter.crclient;

import com.collectrouter.nwe.HMNWEClient;
import com.collectrouter.nwp.HMNWPClient;

/**
 * Created by rom on 12/24 0024.
 */
public class CRCliRoot {
    private static CRCliRoot mInst = null;
    public HMNWPClient mNWPClient;
    public SocketTestActivity mSocketTestActivity;

    private CRCliRoot() {
        mNWPClient = new HMNWPClient( new CRCliEventHandler( this ) );
    }

    public static CRCliRoot getInstance() {
        if ( mInst == null ) {
            mInst = new CRCliRoot();
        }
        return mInst;
    }

    void setSocketTestActivity( SocketTestActivity inst ) {
        mSocketTestActivity = inst;
    }
}

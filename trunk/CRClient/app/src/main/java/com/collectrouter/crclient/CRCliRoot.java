package com.collectrouter.crclient;

import com.collectrouter.nwe.HMNWEClient;
import com.collectrouter.nwp.HMNWPClient;

/**
 * Created by rom on 12/24 0024.
 */
public class CRCliRoot {
    // frame.
    public CREventDepot mEventDepot;
    public CRRMsgHandlerDepot mRMsgHandlerDepot;
    public CRCliUIDepot mUIDepot;
    public HMNWPClient mNWPClient;
    public SocketTestActivity mSocketTestActivity;
    //
    private static CRCliRoot mInst = null;
    private CRRMsgParser mRMsgParser;
    private CRCliCommMonitor mCommMonitor;
    // modules.
    private CRModuleLogin mModuleLogin;
    private CRModuleAccountReg mModuleAccountReg;

    private CRCliRoot() {
       //mNWPClient = new HMNWPClient( new CRCliEventHandler4SocketTest() );
        mEventDepot = new CREventDepot();
        mRMsgHandlerDepot = new CRRMsgHandlerDepot( mEventDepot );
        mUIDepot = new CRCliUIDepot();
        mNWPClient = new HMNWPClient( new CRCliEventHandler() );
        mCommMonitor = new CRCliCommMonitor( mNWPClient );
        new Thread( mCommMonitor ).start();
        //
        mRMsgParser = new CRRMsgParser( mEventDepot );
        mModuleLogin = new CRModuleLogin( mEventDepot, mRMsgHandlerDepot );
        mModuleAccountReg = new CRModuleAccountReg( mEventDepot, mRMsgHandlerDepot );
    }


    public static CRCliRoot getInstance() {
        if ( mInst == null ) {
            mInst = new CRCliRoot();
        }
        return mInst;
    }

}

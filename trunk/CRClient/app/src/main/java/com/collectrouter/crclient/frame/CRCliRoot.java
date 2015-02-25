package com.collectrouter.crclient.frame;

import com.collectrouter.crclient.module.CRModuleAccountReg;
import com.collectrouter.crclient.module.CRModuleAttetion;
import com.collectrouter.crclient.module.CRModuleLogin;
import com.collectrouter.crclient.module.CRModulePublish;
import com.collectrouter.crclient.ui.SocketTestActivity;
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
    public CRCliData mData;
    //
    private static CRCliRoot mInst = null;
    private CRRMsgParser mRMsgParser;
    private CRCliCommMonitor mCommMonitor;
    // modules.
    public CRModuleLogin mModuleLogin;
    private CRModuleAccountReg mModuleAccountReg;
    public CRModuleAttetion mModuleAttetion;
    public CRModulePublish mModulePublish;

    private CRCliRoot() {
       //mNWPClient = new HMNWPClient( new CRCliEventHandler4SocketTest() );
        mEventDepot = new CREventDepot();
        mRMsgHandlerDepot = new CRRMsgHandlerDepot( mEventDepot );
        mUIDepot = new CRCliUIDepot();
        mNWPClient = new HMNWPClient( new CRCliEventHandler() );
        mCommMonitor = new CRCliCommMonitor( mNWPClient );
        mData = new CRCliData( mRMsgHandlerDepot );
        //
        mRMsgParser = new CRRMsgParser( mEventDepot );
        mModuleLogin = new CRModuleLogin( mEventDepot, mRMsgHandlerDepot );
        mModuleAccountReg = new CRModuleAccountReg( mEventDepot, mRMsgHandlerDepot );
        mModuleAttetion = new CRModuleAttetion( mEventDepot, mRMsgHandlerDepot );
        mModulePublish = new CRModulePublish( mEventDepot, mRMsgHandlerDepot );

        // do some init jobs.
        new Thread( mCommMonitor ).start();
    }


    public static CRCliRoot getInstance() {
        if ( mInst == null ) {
            mInst = new CRCliRoot();
        }
        return mInst;
    }

}

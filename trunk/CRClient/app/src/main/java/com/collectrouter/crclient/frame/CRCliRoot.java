package com.collectrouter.crclient.frame;

import com.collectrouter.crclient.module.CRModuleAccountReg;
import com.collectrouter.crclient.module.CRModuleAttation;
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
    public CRAccountData mAccountData;
    //
    private static CRCliRoot mInst = null;
    private CRRMsgParser mRMsgParser;
    private CRCliCommMonitor mCommMonitor;
    // modules.
    private CRModuleLogin mModuleLogin;
    private CRModuleAccountReg mModuleAccountReg;
    private CRModuleAttation mModuleAttation;
    private CRModulePublish mModulePublish;

    private CRCliRoot() {
       //mNWPClient = new HMNWPClient( new CRCliEventHandler4SocketTest() );
        mAccountData = new CRAccountData();
        mEventDepot = new CREventDepot();
        mRMsgHandlerDepot = new CRRMsgHandlerDepot( mEventDepot );
        mUIDepot = new CRCliUIDepot();
        mNWPClient = new HMNWPClient( new CRCliEventHandler() );
        mCommMonitor = new CRCliCommMonitor( mNWPClient );
        //
        mRMsgParser = new CRRMsgParser( mEventDepot );
        mModuleLogin = new CRModuleLogin( mEventDepot, mRMsgHandlerDepot );
        mModuleAccountReg = new CRModuleAccountReg( mEventDepot, mRMsgHandlerDepot );
        mModuleAttation = new CRModuleAttation( mEventDepot );
        mModulePublish = new CRModulePublish( mEventDepot );

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

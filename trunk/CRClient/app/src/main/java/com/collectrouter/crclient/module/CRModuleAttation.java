package com.collectrouter.crclient.module;

import android.app.Activity;

import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;
import com.collectrouter.crclient.frame.CREventDepot;
import com.collectrouter.crclient.frame.CREventHandler;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by apple on 15/1/8.
 */
public class CRModuleAttation implements CREventHandler {

    public CRModuleAttation( CREventDepot eventDepot ) {
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_ENTER_ATTATION, this );
    }

    private void onBtnClickAttation() {
        Activity activityMain = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );

        IntentIntegrator integrator = new IntentIntegrator( activityMain );
        integrator.initiateScan( IntentIntegrator.QR_CODE_TYPES );
    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_BTNCLICK_ENTER_ATTATION: {
                onBtnClickAttation();
            }
            break;
            default:
            break;
        }
    }
}

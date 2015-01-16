package com.collectrouter.crclient.module;

import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CREventDepot;
import com.collectrouter.crclient.frame.CREventHandler;

/**
 * Created by apple on 15/1/8.
 */
public class CRModuleAttation implements CREventHandler {

    public CRModuleAttation( CREventDepot eventDepot ) {
        eventDepot.regEventHandler( CRCliDef.CREVT_BTNCLICK_ENTER_ATTATION, this );
    }

    private void onBtnClickAttation() {
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

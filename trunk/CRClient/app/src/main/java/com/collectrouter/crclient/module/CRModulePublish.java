package com.collectrouter.crclient.module;

import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CREventHandler;

/**
 * Created by apple on 15/1/8.
 */
public class CRModulePublish implements CREventHandler{

    private void onBtnClickPublish() {

    }

    @Override
    public void onEvent(int nEventID, Object param1, Object param2) {
        switch ( nEventID ) {
            case CRCliDef.CREVT_BTNCLICK_PUBLISH:{
                onBtnClickPublish();
            }
            break;
            default:
            break;
        }
    }
}

package com.collectrouter.nwp;

/**
 * Created by rom on 12/26 0026.
 */
public interface HMNWPUnPackStateBase {
    public HMNWPDef.ENUMUNPACKSTATETYPE getStatus();
    int parseRawData( byte[] rawBuf, int nBufValidSize, int nPosStart );
    boolean isComplete();
}

package com.collectrouter.nwp;

/**
 * Created by rom on 12/26 0026.
 */
public class HMNWPUPStateSavePayload implements HMNWPUnPackStateBase {
    HMNWPUnPackImpl mUnPackImpl;
    int mPosFillStart;


    public HMNWPUPStateSavePayload( HMNWPUnPackImpl impl ) {
        mUnPackImpl = impl;
        mUnPackImpl.mBufPayload = new byte[ mUnPackImpl.mNWPHeader.m_lenPayload ];
        mPosFillStart = 0;
    }


    public int fillPayloadBuf( byte[] rawBuf, int nBufValidSize, int nPosRawBuf ) {
        if ( nBufValidSize <= nPosRawBuf ) {
            assert( false );
            return 0;
        }
        if ( mPosFillStart < 0 || mPosFillStart >= mUnPackImpl.mNWPHeader.m_lenPayload ) {
            assert( false );
            return 0;
        }
        int nLenNeedBuf = mUnPackImpl.mNWPHeader.m_lenPayload - mPosFillStart;
        int nLenCanEat = nBufValidSize - nPosRawBuf;
        int nLenEat = ( nLenCanEat < nLenNeedBuf ) ? nLenCanEat : nLenNeedBuf;
        if ( nLenEat <= 0 ) {
            assert( false );
            return 0;
        }
        System.arraycopy( rawBuf, nPosRawBuf, mUnPackImpl.mBufPayload, mPosFillStart, nLenEat );

        // adjust mPosFillStart .
        mPosFillStart += nLenEat;
        return nLenEat;
    }

    @Override
    public HMNWPDef.ENUMUNPACKSTATETYPE getStatus() {
        return HMNWPDef.ENUMUNPACKSTATETYPE.EUNPACKSTATUS_SAVEPAYLOAD;
    }

    @Override
    public int parseRawData(byte[] rawBuf, int nBufValidSize, int nPosStart ) {
        return fillPayloadBuf( rawBuf, nBufValidSize, nPosStart );
    }

    @Override
    public boolean isComplete() {
        return mPosFillStart == mUnPackImpl.mNWPHeader.m_lenPayload;
    }
}

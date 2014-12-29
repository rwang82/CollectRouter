package com.collectrouter.nwp;

/**
 * Created by rom on 12/29 0029.
 */
public class HMNWPUPStateCheckCRC implements HMNWPUnPackStateBase {
    HMNWPUnPackImpl mUnPackImpl;
    byte[] mBufCRC;
    int mPosFillStart;

    public HMNWPUPStateCheckCRC( HMNWPUnPackImpl impl ) {
        mUnPackImpl = impl;
        mBufCRC = new byte[ HMNWPDef.HMNWP_LEN_CRC16 ];
        mPosFillStart = 0;
    }

    boolean checkCRC() {
        if ( mPosFillStart != HMNWPDef.HMNWP_LEN_CRC16 )
            return false;
        int nLenPayload = mUnPackImpl.mNWPHeader.m_lenPayload;
        if ( nLenPayload == 0 )
            return false;
        short sCRC16 = HMNWPCRC16.CalcCRC16( mUnPackImpl.mBufPayload, 0, nLenPayload );

        return ( mBufCRC[ 0 ] == (byte)sCRC16 )
                && ( mBufCRC[ 1 ] == (byte)(sCRC16 >>> 8) );
    }

    @Override
    public HMNWPDef.ENUMUNPACKSTATETYPE getStatus() {
        return HMNWPDef.ENUMUNPACKSTATETYPE.EUNPACKSTATUS_CHECKCRC;
    }

    @Override
    public int parseRawData(byte[] rawBuf, int nBufValidSize, int nPosStart) {
        int nLenNeedBuf = HMNWPDef.HMNWP_LEN_CRC16 - mPosFillStart;
        int nLenCanEat = nBufValidSize - nPosStart;
        int nLenEat = ( nLenCanEat < nLenNeedBuf ) ? nLenCanEat : nLenNeedBuf;
        if ( nLenEat <= 0 ) {
            assert( false );
            return 0;
        }

        System.arraycopy( rawBuf, nPosStart, mBufCRC, mPosFillStart, nLenEat );

        // adjust mPosFillStart .
        mPosFillStart += nLenEat;

        if ( isComplete() ) {
            if ( !checkCRC() ) {
                return 0; // means package format error.
            }
        }

        return nLenEat;
    }

    @Override
    public boolean isComplete() {
        return mPosFillStart == HMNWPDef.HMNWP_LEN_CRC16;
    }
}

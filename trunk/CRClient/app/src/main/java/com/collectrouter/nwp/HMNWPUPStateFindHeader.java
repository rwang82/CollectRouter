package com.collectrouter.nwp;

/**
 * Created by rom on 12/26 0026.
 */
public class HMNWPUPStateFindHeader implements HMNWPUnPackStateBase {
    HMNWPUnPackImpl mUnPackImpl;
    byte[] mBufHeader;
    int mPosFillStart;

    public HMNWPUPStateFindHeader( HMNWPUnPackImpl impl ) {
        mUnPackImpl = impl;
        mBufHeader = new byte[ HMNWPHeader.getLength() ];
    }

    private void reset() {
        mPosFillStart = 0;
    }

    private int fillHeader( byte[] rawBuf, int nBufValidSize, int nPosStart ) {
        int nLenCanFill = nBufValidSize - nPosStart;
        int nLenNeedFill = HMNWPHeader.getLength() - mPosFillStart;
        int nLenEat = nLenCanFill > nLenNeedFill ? nLenNeedFill : nLenCanFill;

        System.arraycopy( rawBuf, nPosStart, mBufHeader, mPosFillStart, nLenEat );

        return nLenEat;
    }

    private int findBeg1Pos( byte[] rawBuf, int nBufValidSize, int nPosStart ) {
        int nLenRawBuf = nBufValidSize;
        if ( nPosStart >= nLenRawBuf ) {
            assert( false );
            return -1;
        }
        for ( int i = nPosStart; i<nLenRawBuf; ++i ) {
            if ( rawBuf[ i ] == HMNWPDef.HMNWP_PACKAGE_BEG1 ) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public HMNWPDef.ENUMUNPACKSTATETYPE getStatus() {
        return HMNWPDef.ENUMUNPACKSTATETYPE.EUNPACKSTATUS_FINDHEADER;
    }


    @Override
    public int parseRawData(byte[] rawBuf, int nBufValidSize, int nPosStart ) {
        int nEat = 0;

        //
        if ( mPosFillStart == 0 ) {
            int nPosBeg1 = findBeg1Pos( rawBuf, nBufValidSize, nPosStart );
            if ( nPosBeg1 < 0 ) { // not find.
                reset();
                nEat += ( nBufValidSize - nPosStart );
                return nEat;
            }
            mBufHeader[ mPosFillStart ] = HMNWPDef.HMNWP_PACKAGE_BEG1;
            mPosFillStart++;
            nEat += (nPosBeg1 - nPosStart + 1);
            return nEat;
        } else if ( mPosFillStart == 1 ) {
            if ( rawBuf[ nPosStart ] != HMNWPDef.HMNWP_PACKAGE_BEG2 ) {
                reset();
                nEat += ( nBufValidSize - nPosStart );
                return nEat;
            }

        }

        int nLenFill = fillHeader(rawBuf, nBufValidSize, nPosStart);
        mPosFillStart += nLenFill;
        nEat += nLenFill;

        if ( isComplete() ) {
            mUnPackImpl.mNWPHeader = new HMNWPHeader( mBufHeader );
        }
        return nEat;
    }


    @Override
    public boolean isComplete() {
        return mPosFillStart == HMNWPHeader.getLength();
    }
}

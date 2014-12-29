package com.collectrouter.nwp;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by rom on 12/23 0023.
 */
public class HMNWPPackage {
    private final static int NWPPACKAGE_FLAG_NONE = 0x00;
    private final static int NWPPACKAGE_FLAG_HEADER_READY = 0x01;
    private final static int NWPPACKAGE_FLAG_PAYLOAD_READY = 0x02;
    private final static int NWPPACKAGE_FLAG_CRC_READY = 0x04;

    byte[] mBufPackage;
    int mLenPackage;
    int mFlag;
    int mPosFillStart;

    public HMNWPPackage( HMNWPHeader nwpHeader, byte[] bufPayload, int nPosBufStart ) {
        assert( nwpHeader.m_lenPayload <= bufPayload.length - nPosBufStart );
        //
        mFlag = NWPPACKAGE_FLAG_HEADER_READY|NWPPACKAGE_FLAG_PAYLOAD_READY|NWPPACKAGE_FLAG_CRC_READY;
        //
        mPosFillStart = -1; // no need fill more.
        //
        short sCRC16 = HMNWPCRC16.CalcCRC16( bufPayload, 0, nwpHeader.m_lenPayload );
        mLenPackage = HMNWPHeader.getLength() + nwpHeader.m_lenPayload + HMNWPDef.HMNWP_LEN_CRC16;
        mBufPackage = new byte[ mLenPackage ];
        // fill package header.
        nwpHeader.fillBuf( mBufPackage );
        // fill package payload.
        System.arraycopy( bufPayload, nPosBufStart, mBufPackage, HMNWPHeader.getLength(), nwpHeader.m_lenPayload );
        // fill crc16
        mBufPackage[ mLenPackage - 2 ] = (byte)sCRC16;
        mBufPackage[ mLenPackage - 1 ] = (byte)(sCRC16 >>> 8);
    }

    public HMNWPPackage( HMNWPHeader nwpHeader ) {
        //
        mFlag = NWPPACKAGE_FLAG_HEADER_READY;
        //
        mPosFillStart = HMNWPHeader.getLength();
        //
        mLenPackage = HMNWPHeader.getLength() + nwpHeader.m_lenPayload + HMNWPDef.HMNWP_LEN_CRC16;
        mBufPackage = new byte[ mLenPackage ];
        // fill package header.
        nwpHeader.fillBuf( mBufPackage );
    }

    public int fillBuf( byte[] rawBuf, int nBufValidSize, int nPosRawBuf ) {
        if ( nBufValidSize <= nPosRawBuf ) {
            assert( false );
            return 0;
        }
        if ( mPosFillStart < 0 || mPosFillStart >= mLenPackage ) {
            assert( false );
            return 0;
        }
        int nLenNeedBuf = getLenNeedBuf();
        int nLenCanEat = nBufValidSize - nPosRawBuf;
        int nLenEat = ( nLenCanEat < nLenNeedBuf ) ? nLenCanEat : nLenNeedBuf;
        if ( nLenEat == 0 ) {
            assert( false );
            return 0;
        }
        System.arraycopy( rawBuf, nPosRawBuf, mBufPackage, mPosFillStart, nLenEat );

        // adjust mPosFillStart and mFlag.
        mPosFillStart += nLenEat;
        if ( mPosFillStart >= mLenPackage - HMNWPDef.HMNWP_LEN_CRC16 ) {
            mFlag |= NWPPACKAGE_FLAG_PAYLOAD_READY;
        }
        if ( mPosFillStart == mLenPackage ) {
            mFlag |= NWPPACKAGE_FLAG_CRC_READY;
        }

        assert( mPosFillStart <= mLenPackage );
        return nLenEat;
    }

    private int getLenNeedBuf() {
        return mLenPackage - mPosFillStart;
    }

    public int getLenPayload() {
        if ( ( mFlag & NWPPACKAGE_FLAG_HEADER_READY ) == 0 ) {
            assert( false );
            return 0;
        }
        int nLenPayload = mBufPackage.length - HMNWPHeader.getLength() - HMNWPDef.HMNWP_LEN_CRC16;
        if ( nLenPayload < 0 ) {
            assert( false );
            return 0;
        }
        return nLenPayload;
    }

    public boolean isValid() {
        return checkCRC();
    }

    private boolean checkCRC() {
        if ( ( mFlag & NWPPACKAGE_FLAG_CRC_READY ) != NWPPACKAGE_FLAG_CRC_READY ) {
            return false;
        }
        int nLenPayload = getLenPayload();
        if ( nLenPayload == 0 )
            return false;
        short sCRC16 = HMNWPCRC16.CalcCRC16( mBufPackage, HMNWPHeader.getLength(), nLenPayload );

        return ( mBufPackage[ mLenPackage - 2 ] == (byte)sCRC16 )
            && ( mBufPackage[ mLenPackage - 1 ] == (byte)(sCRC16 >>> 8) );
    }

    public byte[] getBufData() {
        assert( isValid() );
        return mBufPackage;
    }

    public int getLenBufData() { return mLenPackage; }



}

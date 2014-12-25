package com.collectrouter.nwp;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by rom on 12/23 0023.
 */
public class HMNWPPackage {
    byte[] mBufPackage;
    int mLenPackage;

    public HMNWPPackage( HMNWPHeader nwpHeader, byte[] bufPayload, int nPosBufStart ) {
        assert( nwpHeader.m_lenPayload <= bufPayload.length - nPosBufStart );
        short sCRC16 = HMNWPCRC16.CalcCRC16( bufPayload, nwpHeader.m_lenPayload );
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

    public byte[] getBufData() { return mBufPackage; }

    public int getLenBufData() { return mLenPackage; }
}

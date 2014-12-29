package com.collectrouter.nwp;

////////////////////////////////////////////////////////
//    unsigned char m_ucBeg1;
//    unsigned char m_ucBeg2;
//    unsigned char m_ucVer;
//    __int16 m_i16BundleBufIndex; // a bundle buffer index.
//    __int16 m_i16PackageIndex; // a big bundle of buffer will separate to a group of packages.
//    __int16 m_i16PackageCount; // the count of group of package to make up a bundle buffer.
//    unsigned char m_ucBodyProtocol;
//    __int16 m_i16PayloadLen;
////////////////////////////////////////////////////////
/**
 * Created by rom on 12/23 0023.
 */
public class HMNWPHeader {
    private byte m_byteBeg1;
    private byte m_byteBeg2;
    private byte m_byteVer;
    public short m_indexBundleBuf;
    public short m_indexPackage;
    public short m_countPackage;
    private byte m_byteBodyProtocol;
    public short m_lenPayload;

    public HMNWPHeader() {
        m_byteBeg1 = HMNWPDef.HMNWP_PACKAGE_BEG1;
        m_byteBeg2 = HMNWPDef.HMNWP_PACKAGE_BEG2;
        m_byteVer = HMNWPDef.HMNWP_PACKAGE_VER;
        m_indexBundleBuf = 0;
        m_indexPackage = 0;
        m_countPackage = 1;
        m_byteBodyProtocol = 0;
        m_lenPayload = 0;
    }

    public HMNWPHeader( byte[] rawBuf ) {
        // server is windows. X86 CPU, so use litter-endian.
        assert( rawBuf.length == getLength() );
        m_byteBeg1 = rawBuf[ 0 ];
        m_byteBeg2 = rawBuf[ 1 ];
        m_byteVer = rawBuf[ 2 ];
        m_indexBundleBuf = rawBuf[ 4 ];
        m_indexBundleBuf <<= 8;
        m_indexBundleBuf |= (short)rawBuf[ 3 ];
        m_indexPackage = rawBuf[ 6 ];
        m_indexPackage <<= 8;
        m_indexPackage |= (short)rawBuf[5];
        m_countPackage = rawBuf[ 8 ];
        m_countPackage <<= 8;
        m_countPackage |= (short)rawBuf[ 7 ];
        m_byteBodyProtocol = rawBuf[ 9 ];
        m_lenPayload = (short)rawBuf[ 11 ];
        m_lenPayload <<= 8;
        m_lenPayload |= rawBuf[ 10 ];


    }

    public void setBundleBufIndex( short indexBundleBuf ) {
        m_indexBundleBuf = indexBundleBuf;
    }

    public void setPackageIndex( short indexPackage ) {
        m_indexPackage = indexPackage;
    }

    public void setPackageCount( short countPackage ) {
        m_countPackage = countPackage;
    }

    public void setPayloadLength( short lenPayload ) {
        m_lenPayload = lenPayload;
    }

    public static int getLength() {
        return 1+1+1+2+2+2+1+2;
    }

    public boolean fillBuf( byte[] bufNeedFill ) {
        if ( bufNeedFill.length < getLength() )
            return false;
        bufNeedFill[ 0 ] = m_byteBeg1;
        bufNeedFill[ 1 ] = m_byteBeg2;
        bufNeedFill[ 2 ] = m_byteVer;
        bufNeedFill[ 3 ] = (byte)(m_indexBundleBuf&0xFF);
        bufNeedFill[ 4 ] = (byte)(m_indexBundleBuf>>>8);
        bufNeedFill[ 5 ] = (byte)(m_indexPackage&0xFF);
        bufNeedFill[ 6 ] = (byte)(m_indexPackage>>>8);
        bufNeedFill[ 7 ] = (byte)(m_countPackage&0xFF);
        bufNeedFill[ 8 ] = (byte)(m_countPackage>>>8);
        bufNeedFill[ 9 ] = m_byteBodyProtocol;
        bufNeedFill[ 10 ] = (byte)(m_lenPayload&0xFF);
        bufNeedFill[ 11 ] = (byte)(m_lenPayload>>>8);
        return true;
    }
}

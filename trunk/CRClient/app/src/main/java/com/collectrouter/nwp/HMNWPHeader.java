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
        bufNeedFill[ 3 ] = (byte)(m_indexBundleBuf>>>8);
        bufNeedFill[ 4 ] = (byte)(m_indexBundleBuf&0xFF);
        bufNeedFill[ 5 ] = (byte)(m_indexPackage>>>8);
        bufNeedFill[ 6 ] = (byte)(m_indexPackage&0xFF);
        bufNeedFill[ 7 ] = (byte)(m_countPackage>>>8);
        bufNeedFill[ 8 ] = (byte)(m_countPackage&0xFF);
        bufNeedFill[ 9 ] = m_byteBodyProtocol;
        bufNeedFill[ 10 ] = (byte)(m_lenPayload>>>8);
        bufNeedFill[ 11 ] = (byte)(m_lenPayload&0xFF);
        return true;
    }
}

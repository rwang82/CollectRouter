package com.collectrouter.nwp;

import java.util.List;

/**
 * Created by rom on 12/23 0023.
 */
public class HMNWPPackImpl {

    public static boolean createPackages( byte[] rawBuf, int nLenRawBuf, List< HMNWPPackage > containerPackage ) {
        if ( ( rawBuf.length < nLenRawBuf ) || nLenRawBuf == 0 )
            return false;
        int nPackageCount = 0;
        int nLenDataNoPacked = nLenRawBuf;



        return true;
//        if ( !pRawBuf || uLenRawData == 0 ) {
//            assert( false );
//            return false;
//        }
//        unsigned int uPackageCount = 0;
//        unsigned int uLenDataNoPacked = uLenRawData;
//        HMNWPPackage* pNWPPackageNew = NULL;
//        const unsigned char* pPosRawData = NULL;
//
//        pPosRawData = pRawBuf;
//        uPackageCount = ( ( uLenRawData - 1 ) / HMNWP_LEN_PAYLOAD_MAX ) + 1;
//        for ( unsigned int uPackageIndex = 0; uPackageIndex != uPackageCount; ++uPackageIndex ) {
//            HMNWPHeader header;
//            header.m_i16PackageCount = uPackageCount;
//            header.m_i16PackageIndex = uPackageIndex;
//            if ( ( uPackageIndex + 1 ) < uPackageCount ) {
//                header.m_i16PayloadLen = HMNWP_LEN_PAYLOAD_MAX;
//            } else {
//                header.m_i16PayloadLen = uLenDataNoPacked;
//            }
//            pNWPPackageNew = new HMNWPPackage( &header, pPosRawData );
//            pPosRawData += header.m_i16PayloadLen;
//            uLenDataNoPacked -= header.m_i16PayloadLen;
//            containerNWPPackage.push_back( pNWPPackageNew );
//        }
//        return true;
    }

}

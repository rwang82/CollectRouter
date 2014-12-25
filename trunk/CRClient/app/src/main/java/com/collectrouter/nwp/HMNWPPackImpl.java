package com.collectrouter.nwp;

import java.util.List;

/**
 * Created by rom on 12/23 0023.
 */
public class HMNWPPackImpl {

    public static boolean createPackages( byte[] rawBuf, int nLenRawBuf, List<HMNWPPackage> containerPackage ) {
        if ( ( rawBuf.length < nLenRawBuf ) || nLenRawBuf == 0 )
            return false;
        int nPackageCount = 0;
        int nLenDataNoPacked = nLenRawBuf;
        int nPosBufStart = 0;
        HMNWPHeader nwpHeader = new HMNWPHeader();

        nPackageCount = ( (nLenRawBuf - 1)/HMNWPDef.HMNWP_LEN_PAYLOAD_MAX ) + 1;
        for ( int nPackageIndex = 0; nPackageIndex != nPackageCount; ++nPackageIndex ) {
            nwpHeader.setPackageCount( (short)nPackageCount );
            nwpHeader.setPackageIndex( (short)nPackageIndex );
            if ( ( nPackageIndex + 1 ) < nPackageCount ) {
                nwpHeader.setPayloadLength( (short)HMNWPDef.HMNWP_LEN_PAYLOAD_MAX );
                nLenDataNoPacked -= HMNWPDef.HMNWP_LEN_PAYLOAD_MAX;
            } else {
                if ( nLenDataNoPacked > HMNWPDef.HMNWP_LEN_PAYLOAD_MAX ) {
                    assert( false );
                    containerPackage.clear();
                    return false;
                }
                nwpHeader.setPayloadLength( (short)nLenDataNoPacked );
            }
            //
            containerPackage.add( new HMNWPPackage( nwpHeader, rawBuf, nPosBufStart ) );
            nPosBufStart += nwpHeader.m_lenPayload;
        }

        return true;
    }

}

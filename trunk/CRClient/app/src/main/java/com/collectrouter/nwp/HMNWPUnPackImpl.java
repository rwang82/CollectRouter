package com.collectrouter.nwp;

import com.collectrouter.common.CRUtil;

/**
 * Created by rom on 12/25 0025.
 */
public class HMNWPUnPackImpl {
    HMNWPCliEventHandler mEventHandler;
    HMNWPUnPackStateBase mUnPackStateCur;
    HMNWPHeader mNWPHeader;
    byte[] mBufPayload;
    HMNWPPayloadCacheDepot mPayloadCacheDepot;

    public HMNWPUnPackImpl( HMNWPCliEventHandler eventHandler )
    {
        mPayloadCacheDepot = new HMNWPPayloadCacheDepot();
        mEventHandler = eventHandler;
        resetState();
    }

    public void onRecv( byte[] rawBuf, int nLenRawBuf ) {
        if ( nLenRawBuf == 0 || rawBuf.length <= 0 ) {
            assert( false );
            return;
        }
        //
        int nPosStart = 0;
        int nLenEat;

        // rawBuf.length is 1024 is a error, need send nLenRawBuf to paraseRawData

        while ( nPosStart < nLenRawBuf ) {
            nLenEat = mUnPackStateCur.parseRawData( rawBuf, nLenRawBuf, nPosStart );
            if ( nLenEat == 0 ) {
                assert( false );
                resetState();
                return;
            }
            nPosStart += nLenEat;
            //
            if ( mUnPackStateCur.isComplete() ) {
                switch2NextState();
            }
        }
    }

    private void switch2NextState() {
        if ( !mUnPackStateCur.isComplete() ) {
            assert( false );
            return;
        }
        HMNWPDef.ENUMUNPACKSTATETYPE eUnPackStatus = mUnPackStateCur.getStatus();

        switch ( eUnPackStatus ) {
            case EUNPACKSTATUS_FINDHEADER:
            {
                mUnPackStateCur = new HMNWPUPStateSavePayload( this );
            }
            break;
            case EUNPACKSTATUS_SAVEPAYLOAD:
            {
                mUnPackStateCur = new HMNWPUPStateCheckCRC( this );
            }
            break;
            case EUNPACKSTATUS_CHECKCRC:
            {
                if ( mNWPHeader.m_indexPackage == 0 ) {
                    mPayloadCacheDepot.clearAll();
                }
                if ( mNWPHeader.m_indexPackage != mPayloadCacheDepot.getCount() ) {
                    // maybe lose package, so needn't save.
                    mPayloadCacheDepot.clearAll();
                } else {
                    // mNWPHeader.m_indexPackage and mPayloadCacheDepot in order.
                    if ( mNWPHeader.m_indexPackage + 1 < mNWPHeader.m_countPackage ) {
                        byte [] item = new byte[ mBufPayload.length ];
                        System.arraycopy( mBufPayload, 0, item, 0, mBufPayload.length );
                        mPayloadCacheDepot.addItem( item );
                    } else {
                        if ( mNWPHeader.m_countPackage == 1 ) {
                            mEventHandler.onRecv( mBufPayload, mBufPayload.length );
                        } else {
                            mPayloadCacheDepot.addItem( mBufPayload );
                            byte[] rawPayload = mPayloadCacheDepot.restoreRawPayload();
                            mPayloadCacheDepot.clearAll();
                            mEventHandler.onRecv( rawPayload, rawPayload.length );
                        }
                    }
                }

                resetState();
            }
            break;
            default:
            {}
        }
    }

    private void resetState() {
        mUnPackStateCur = new HMNWPUPStateFindHeader( this );
        mNWPHeader = null;
        mBufPayload = null;
    }

}

package com.collectrouter.nwp;

import com.collectrouter.nwe.HMNWECliDef;

/**
 * Created by rom on 12/23 0023.
 */
public class HMNWPDef {

    public enum ENUMUNPACKSTATETYPE {
        EUNPACKSTATUS_FINDHEADER,
        EUNPACKSTATUS_SAVEPAYLOAD,
        EUNPACKSTATUS_CHECKCRC,
    }

    public enum ENUMRAWBUFTYPE {
        ERAWBUF_TYPE_UNKNOWN,
        ERAWBUF_TYPE_NOTIN,
        ERAWBUF_TYPE_BODYIN_ONLY,
        ERAWBUF_TYPE_TAILIN_ONLY,
        ERAWBUF_TYPE_ALLIN,
        ERAWBUF_TYPE_HEADIN_ONLY
    }

    public final static int HMNWP_LEN_CRC16 = 2;
    public final static int HMNWP_LEN_PAYLOAD_MAX;
    public final static byte HMNWP_PACKAGE_BEG1 = (byte)0xAF;
    public final static byte HMNWP_PACKAGE_BEG2 = (byte)0xE0;
    public final static byte HMNWP_PACKAGE_VER = (byte)0x01;
    static
    {
        HMNWP_LEN_PAYLOAD_MAX = HMNWECliDef.BUFSIZE_RECV_NWDATA - HMNWPHeader.getLength() - HMNWP_LEN_CRC16;
    }
}

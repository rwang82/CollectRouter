package com.collectrouter.common;

import com.collectrouter.crclient.R;

public class CRUtil {
    public static boolean BitHitTest( byte val, byte bit ) {
        return ( val & bit ) == bit;
    }

}
package com.collectrouter.common;

public class CRUtil {
    public static boolean BitHitTest( byte val, byte bit ) {
        return ( val & bit ) == bit;
    }
}
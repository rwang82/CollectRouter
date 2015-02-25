package com.collectrouter.common;

import com.collectrouter.crclient.R;

import java.util.List;

public class CRUtil {
    public static boolean BitHitTest( byte val, byte bit ) {
        return ( val & bit ) == bit;
    }

    public static boolean IsExist( List< String > listStr, String strDest ) {
        for ( String item : listStr ) {
            if ( item.equals( strDest ) )
                return true;
        }

        return false;
    }
}
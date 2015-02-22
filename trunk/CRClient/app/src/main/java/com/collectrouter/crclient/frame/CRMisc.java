package com.collectrouter.crclient.frame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import com.collectrouter.crclient.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rom on 1/15 0015.
 */
public class CRMisc {
    public static boolean saveBitmap2SDCard( Bitmap bitmap, String destDir, String destFileName ) {
        if ( !android.os.Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED ) )
            return false;
        boolean bResult = false;
        File dir = new File( destDir );
        if ( !dir.exists() ) {
            dir.mkdir();
        }
        //
        File destFile = new File( destDir, destFileName );
        FileOutputStream foutputStream = null;
        try {
            foutputStream = new FileOutputStream( destFile );
            if ( bitmap != null ) {
                if ( bitmap.compress( Bitmap.CompressFormat.PNG, 100, foutputStream ) ) {
                    foutputStream.flush();
                    foutputStream.close();
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                foutputStream.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }

        return bResult;
    }

    public static String getProductSort( int nSortType ) {
        if ( nSortType < 0 )
            return "";
        Activity activityMain = CRCliRoot.getInstance().mUIDepot.getActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        int nId = R.string.ps_name_1;
        String strDest =activityMain.getResources().getString(R.string.ps_name_1 + nSortType );
        String strTest = activityMain.getResources().getString( R.string.ps_name_1 );
        return strDest;
    }
}

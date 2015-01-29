package com.collectrouter.crclient.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.collectrouter.crclient.R;

/**
 * Created by rom on 1/26 0026.
 */
public class BtnGoUpLevel extends View {
    public BtnGoUpLevel(Context context) {
        super(context);
    }
    public BtnGoUpLevel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BtnGoUpLevel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw( Canvas canvas ) {
        int nWidth = getWidth();
        int nHeight = getHeight();
        Paint paint = new Paint();
        paint.setColor( getResources().getColor(R.color.prime_title_text ) );
        paint.setStrokeWidth( 4 );
        int nSpaceH = nWidth/4;
        int nSpaceV = nHeight/4;

        Path path = new Path();
        path.moveTo( nWidth - nSpaceH, 0 + nSpaceV );
        path.lineTo( nWidth - nSpaceH, nHeight - nSpaceV );
        path.lineTo( 0 + nSpaceH, nHeight/2 );
        path.close();
        canvas.drawPath( path, paint );
    }
}

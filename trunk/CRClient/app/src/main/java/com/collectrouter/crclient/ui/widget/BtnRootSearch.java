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
public class BtnRootSearch extends View {
    public BtnRootSearch(Context context) {
        super(context);
    }
    public BtnRootSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BtnRootSearch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw( Canvas canvas ) {
        int nLineWidth = 4;
        int nWidth = getWidth();
        int nHeight = getHeight();
        int nRadio = (nWidth / 3 - nLineWidth);
        Paint paint = new Paint();
        paint.setColor( getResources().getColor(R.color.prime_title_text ) );
        paint.setStrokeWidth( nLineWidth );
        paint.setStyle(Paint.Style.STROKE );

        canvas.drawCircle(nWidth / 2, nHeight / 2, nRadio, paint);
        Paint paintLine = new Paint();
        paintLine.setColor( getResources().getColor( R.color.prime_title_text ) );
        paintLine.setStrokeWidth( 8 );
        canvas.drawLine( nWidth*2/3, nHeight*2/3, nWidth*3/4, nHeight*3/4, paintLine );
    }
}

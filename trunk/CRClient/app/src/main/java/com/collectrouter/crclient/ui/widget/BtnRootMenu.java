package com.collectrouter.crclient.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.collectrouter.crclient.R;

/**
 * Created by rom on 1/26 0026.
 */
public class BtnRootMenu extends View {

    public BtnRootMenu(Context context) {
        super(context);
    }
    public BtnRootMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BtnRootMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw( Canvas canvas ) {
        int nWidth = getWidth();
        int nHeight = getHeight();
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.prime_title_text));
        paint.setStrokeWidth(4);
        Point ptLeft = new Point( nWidth/4, nHeight*2/3 );
        Point ptMid = new Point( nWidth/2, nHeight*2/3 );
        Point ptRight = new Point( nWidth*3/4, nHeight*2/3 );
        int nRadio = 4;

//        canvas.drawLine( 0, 0, nWidth, 0, paint );
//        canvas.drawLine( nWidth, 0, nWidth, nHeight, paint );
//        canvas.drawLine( nWidth, nHeight, 0, nHeight, paint );
//        canvas.drawLine( 0, nHeight, 0, 0, paint );
        //
        canvas.drawCircle( ptLeft.x, ptLeft.y, nRadio, paint);
        canvas.drawCircle( ptMid.x, ptMid.y, nRadio, paint);
        canvas.drawCircle( ptRight.x, ptRight.y, nRadio, paint);
    }
}

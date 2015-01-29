package com.collectrouter.crclient.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.collectrouter.crclient.R;

/**
 * Created by rom on 1/26 0026.
 */
public class BtnShowDrawer extends View {

    public BtnShowDrawer(Context context){
        super( context );
    }

    public BtnShowDrawer( Context context, AttributeSet attrs ) {
        super(context, attrs);
//        for (int i = 0; i < attrs.getAttributeCount(); i++) {
//            if ("color".equals(attrs.getAttributeName(i))) {
//                String strData = attrs.getAttributeValue(i);
//
//                int a = 0;
//                //Integer.parseInt
//            }
//        }
    }

    public BtnShowDrawer( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );

    }

    @Override
    public void onDraw( Canvas canvas ) {
        int nWidth = getWidth();
        int nHeight = getHeight();
        Paint paint = new Paint();
        paint.setColor( getResources().getColor(R.color.prime_title_text ) );
        paint.setStrokeWidth( 4 );
//        canvas.drawLine( 0, 0, nWidth, 0, paint );
//        canvas.drawLine( nWidth, 0, nWidth, nHeight, paint );
//        canvas.drawLine( nWidth, nHeight, 0, nHeight, paint );
//        canvas.drawLine( 0, nHeight, 0, 0, paint );
        //
        int nXStart = nWidth/4;
        int nXEnd = nWidth*3/4;
        int nYStart = nHeight/3;
        int nYMid = nHeight/2;
        int nYEnd = nHeight*2/3;
        canvas.drawLine( nXStart, nYStart, nXEnd, nYStart, paint );
        canvas.drawLine( nXStart, nYMid, nXEnd, nYMid, paint );
        canvas.drawLine( nXStart, nYEnd, nXEnd, nYEnd, paint );
    }

}

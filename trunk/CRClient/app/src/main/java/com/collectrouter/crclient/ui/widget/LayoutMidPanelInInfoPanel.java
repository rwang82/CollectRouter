package com.collectrouter.crclient.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.collectrouter.crclient.R;

/**
 * Created by rom on 1/28 0028.
 */
public class LayoutMidPanelInInfoPanel extends LinearLayout {

    public LayoutMidPanelInInfoPanel(Context context) {
        super(context);
        setWillNotDraw( false );
    }

    public LayoutMidPanelInInfoPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw( false );
    }

    public LayoutMidPanelInInfoPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw( false );
    }

    @Override
    protected void onDraw( Canvas canvas ) {

        int nWidth = getWidth() - 1;
        int nHeight = getHeight() - 1;
        Paint paint = new Paint();
        paint.setStrokeWidth( 3 );
        paint.setColor( getResources().getColor( R.color.prime_title_bk ) );
        // draw rect frame.
        canvas.drawLine( 0, 0, 0, nHeight, paint );
        canvas.drawLine( 0, nHeight, nWidth, nHeight, paint );
        canvas.drawLine( nWidth, nHeight, nWidth, 0, paint );
        canvas.drawLine( nWidth, 0, 0, 0, paint );
        // draw dividing line.
        View layoutInfo = findViewById( R.id.info_attation_publish );
        int nHeightDividingLine = layoutInfo.getHeight() - 5;
        if ( nHeightDividingLine < 0 )
            nHeightDividingLine = 0;
        canvas.drawLine( nWidth/3, 4, nWidth/3, nHeightDividingLine, paint );
        canvas.drawLine( nWidth*2/3, 4, nWidth*2/3, nHeightDividingLine, paint );
        // draw fill function district.
        canvas.drawRect( 0, layoutInfo.getHeight(), nWidth, nHeight, paint );
        //
        View layoutFunction = findViewById( R.id.info_function );
        Paint paintBk = new Paint();
        paintBk.setStrokeWidth( 2 );
        paintBk.setColor( getResources().getColor( R.color.prime_backgroud ) );
        int nHeightFunction = layoutFunction.getHeight();
        canvas.drawLine( nWidth/2, layoutInfo.getHeight()+2, nWidth/2, nHeight-2, paintBk );
        // draw vertical line
        super.onDraw( canvas );
    }
}

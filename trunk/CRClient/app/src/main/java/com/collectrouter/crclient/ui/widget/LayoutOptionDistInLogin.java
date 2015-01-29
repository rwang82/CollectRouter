package com.collectrouter.crclient.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.collectrouter.crclient.R;

/**
 * Created by rom on 1/28 0028.
 */
public class LayoutOptionDistInLogin extends RelativeLayout{

    public LayoutOptionDistInLogin(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public LayoutOptionDistInLogin(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public LayoutOptionDistInLogin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw( Canvas canvas ) {

        CheckBox cbUserName = (CheckBox)findViewById( R.id.cb_save_username );
        CheckBox cbPassword = (CheckBox)findViewById( R.id.cb_save_password );
        // draw background.
        Paint paintBK = new Paint();
        paintBK.setColor( getResources().getColor( R.color.prime_title_bk ) );
        canvas.drawRect( 0, 0, getWidth(), getHeight(), paintBK );
        //
        Paint paintLine = new Paint();
        paintLine.setColor( getResources().getColor( R.color.prime_backgroud ) );
        paintLine.setStrokeWidth( 4 );
        int nXPos = cbUserName.getWidth() + cbPassword.getWidth() + 10;
        canvas.drawLine( nXPos, 0, nXPos, getHeight(), paintLine );
        // tvRegister.getWidth()
    }


}

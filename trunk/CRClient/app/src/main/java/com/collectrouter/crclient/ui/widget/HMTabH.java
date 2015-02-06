package com.collectrouter.crclient.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;

import com.collectrouter.crclient.R;

/**
 * Created by rom on 1/28 0028.
 */
public class HMTabH extends View {

    public interface TabAdapter {
        public int getCount();
        public String getItemText( int position );
        public int getItemId( int positon );
    }

    public interface OnSelectChangeListener {
        public void onSelectChange( int position );
    }

    OnSelectChangeListener mOnSelectChangeListener = null;
    TabAdapter mAdapter = null;
    Drawable mbkDrawObj = null;
    Drawable mftDrawObj = null;
    int mIndexSel = 0;
    int mIndexTouchDown = -1;

    public HMTabH(Context context) {
        super(context);
    }

    public HMTabH(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HMTabH(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSelectChangeListener( OnSelectChangeListener onSelectChangeListener ) {
        mOnSelectChangeListener = onSelectChangeListener;
    }

    public void setAdapter( TabAdapter adapter ) {
        mAdapter = adapter;
    }

    public void setBKDrawObj( Drawable bkObj ) {
        mbkDrawObj = bkObj;
    }

    public void setFTDrawObj( Drawable ftObj ) {
        mftDrawObj = ftObj;
    }

    @Override
    public void onDraw( Canvas canvas ) {
        int nWidth = getWidth();
        int nHeight = getHeight();

        // draw background drawable object.
        if ( mbkDrawObj != null ) {
            mbkDrawObj.draw( canvas );
        } else {
            // draw background.
            Paint paintBK = new Paint();
            paintBK.setColor( getResources().getColor(R.color.prime_title_bk ) );
            canvas.drawRect( 0, 0, nWidth, nHeight, paintBK );
        }

        //
        if ( mAdapter != null ) {
            int nCount = mAdapter.getCount();
            if (nCount > 0) {
                int posX = 0;
                int posY = 0;
                int nItemWidth = nWidth / nCount;

                // draw item text
                Rect rcBound = new Rect();
                Paint paintText = new Paint();
                for (int nIndex = 0; nIndex < nCount; ++nIndex) {
                    String strItemText = mAdapter.getItemText(nIndex);
                    if ( nIndex == mIndexSel ) {
                        paintText.setTextSize( 45 );
                        paintText.setColor( getResources().getColor( R.color.tab_text_selected ) );
                    } else {
                        paintText.setTextSize(40);
                        paintText.setColor(getResources().getColor(R.color.tab_text_unselected));
                    }
                    paintText.getTextBounds( strItemText, 0, strItemText.length(), rcBound );
                    posX = nItemWidth / 2 + nWidth * nIndex / nCount - rcBound.width()/2;
                    posY = nHeight / 2 + rcBound.height()/2;
                    canvas.drawText( strItemText, posX, posY, paintText);
                }

                // draw underline.
                if ( mIndexSel < nCount ) {
                    Paint paintUnderLine = new Paint();
                    paintUnderLine.setColor( getResources().getColor( R.color.tab_underline ) );
                    int nPosUnderLineTop = nHeight - 15;
                    canvas.drawRect( mIndexSel*nItemWidth, nPosUnderLineTop, (mIndexSel+1)*nItemWidth, nHeight, paintUnderLine );
                }
            }
        }else {
            super.onDraw( canvas );
        }

        // draw frontground drawable object.
        if ( mftDrawObj != null ) {
            mbkDrawObj.draw( canvas );
        }
    }

    @Override
    public boolean callOnClick() {
        return super.callOnClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:{
                //Log.e(TAG, "onTouchEvent ACTION_DOWN");
                int xPos = Math.round( event.getX() );
                int yPos = Math.round( event.getY() );

                mIndexTouchDown = hitTestItem( xPos, yPos );
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                //Log.e(TAG, "onTouchEvent ACTION_MOVE");
            }
                break;
            case MotionEvent.ACTION_UP: {
                //Log.e(TAG, "onTouchEvent ACTION_UP");
                int xPos = Math.round(event.getX());
                int yPos = Math.round(event.getY());
                int indexTouchUp = hitTestItem(xPos, yPos);
                if ( indexTouchUp < 0 ) {
                    break;
                }
                if ( indexTouchUp == mIndexTouchDown ) {
                    //
                    mIndexSel = indexTouchUp;
                    //
                    if ( mOnSelectChangeListener != null ) {
                        mOnSelectChangeListener.onSelectChange( mIndexSel );
                    }
                    //
                    invalidate();
                }
            }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    int hitTestItem( int x, int y ) {
        if ( mAdapter == null )
            return -1;
        int nCount = mAdapter.getCount();
        if ( nCount <= 0 )
            return -1;
        int nWidth = getWidth();
        int nHeight = getHeight();
        int nItemWidth = nWidth/nCount;
        for ( int nIndex = 0; nIndex<nCount; ++nIndex ) {
            if ( x > nIndex*nItemWidth && x <(nIndex+1)*nItemWidth )
                return nIndex;
        }
        return -1;
    }
}

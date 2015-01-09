package com.collectrouter.crclient.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;

/**
 * Created by apple on 15/1/8.
 */
public class FragmentInfoPanel extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_infopanel, container, false);
        //
        viewRoot.setOnTouchListener( mTouchListenerRootView );

        viewRoot.findViewById( R.id.btn_attation ).setOnClickListener( mClickListenerBtnAttation );
        viewRoot.findViewById( R.id.btn_publish ).setOnClickListener(mClickListenerBtnPublish);

        return viewRoot;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private View.OnTouchListener mTouchListenerRootView = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    private View.OnClickListener mClickListenerBtnAttation = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            CRCliRoot.getInstance().mEventDepot.fire(CRCliDef.CREVT_BTNCLICK_ATTATION, 0, 0);
        }
    };

    private View.OnClickListener mClickListenerBtnPublish = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_PUBLISH, 0, 0 );
        }
    };


}

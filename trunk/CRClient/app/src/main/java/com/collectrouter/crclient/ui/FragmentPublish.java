package com.collectrouter.crclient.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;

/**
 * Created by rom on 1/13 0013.
 */
public class FragmentPublish extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View viewRoot = inflater.inflate(R.layout.fragment_publish, container, false);

        viewRoot.findViewById( R.id.btn_publish_by_photograph ).setOnClickListener( mClickListenerPhotograph );
        viewRoot.findViewById( R.id.btn_publish_by_select_picture ).setOnClickListener( mClickListenerSelectPicture );
        viewRoot.findViewById( R.id.btn_publish_by_scan ).setOnClickListener( mClickListenerScan );
        viewRoot.findViewById( R.id.btn_publish_commit ).setOnClickListener( mClickListenPublishCommit );
        //
        CRCliRoot.getInstance().mUIDepot.regFragment( CRCliDef.CRCLI_FRAGMENT_PUBLISH, this );

        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        CRCliRoot.getInstance().mUIDepot.unRegFragment( CRCliDef.CRCLI_FRAGMENT_PUBLISH );

        super.onDestroyView();

    }

    private View.OnClickListener mClickListenerPhotograph = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_PHOTOGRAPH_4_PUBLISH, 0, 0 );
        }
    };

    private View.OnClickListener mClickListenerSelectPicture = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_SELECTPICTURE_4_PUBLISH, 0, 0 );

        }
    };

    private View.OnClickListener mClickListenerScan = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_SCAN_4_PUBLISH, 0, 0 );

        }
    };

    private View.OnClickListener mClickListenPublishCommit = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // need more code here, to check user input.

            CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_BTNCLICK_COMMIT_PUBLISH, 0, 0 );
        }
    };
}

package com.collectrouter.crclient.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;

/**
 * Created by rom on 2/1 0001.
 */
public class FragmentDoAttetion extends Fragment {
    public final static String TAG = CRCliDef.CRCLI_FRAGMENT_DO_ATTETION;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View viewRoot = inflater.inflate(R.layout.fragment_attation, null );

        viewRoot.findViewById( R.id.btn_attetion ).setOnClickListener( mClickListener4BtnAttetion );

        //
        CRCliRoot.getInstance().mUIDepot.regFragment( TAG, this );
        return viewRoot;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    private View.OnClickListener mClickListener4BtnAttetion = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            TextView tvDestUserName = (TextView)FragmentDoAttetion.this.getActivity().findViewById( R.id.et_dest_username );
            String strDestUserName = tvDestUserName.getText().toString();

            CRCliRoot.getInstance().mModuleAttetion.addAttetion( strDestUserName );
        }
    };
}

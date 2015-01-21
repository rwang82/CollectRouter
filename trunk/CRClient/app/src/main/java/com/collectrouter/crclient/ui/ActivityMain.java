package com.collectrouter.crclient.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.collectrouter.crclient.R;
import com.collectrouter.crclient.frame.CRCliDef;
import com.collectrouter.crclient.frame.CRCliRoot;

/**
 * Created by rom on 1/6 0006.
 */
public class ActivityMain extends ActionBarActivity {

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );
        CRCliRoot.getInstance().mUIDepot.regActivity(CRCliDef.CRCLI_ACTIVITY_MAIN, this );

        // create left drawer panel.
        createInfoPanel();
        // create top navigate header.
        createNavigateHeader();
        // switch 2 login panel.
        switch2LoginFragment();

        long lTId = Thread.currentThread().getId();

        int a = 0;
    }

    @Override
    public void onDestroy() {
        CRCliRoot.getInstance().mUIDepot.unRegActivity( CRCliDef.CRCLI_ACTIVITY_MAIN );
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if(keyCode== android.view.KeyEvent.KEYCODE_BACK ) {
            if ( event.getRepeatCount() == 0 ) {
                if ( getFragmentManager().getBackStackEntryCount() > 1 ) {
                    getFragmentManager().popBackStack();
                } else {
                    AlertDialog.Builder alertbBuilder=new AlertDialog.Builder( ActivityMain.this);
                    alertbBuilder.setTitle("提示").setMessage("确认退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //结束这个Activity
                            ActivityMain.this.finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    }).create();
                    alertbBuilder.show();
                }
            }

            return true;
        }

        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch ( requestCode ) {
            case CRCliDef.CRREQUESTCODE_PHTOGRAPH4PUBLISH:
            {
                CRCliRoot.getInstance().mEventDepot.fire( CRCliDef.CREVT_RECV_PHOTOGRAPH_4_PUBLISH, resultCode, data );
            }
            break;
            default:
                break;
        }

//        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
//            case RESULT_OK:
//                Bundle b = data.getExtras();  //data为B中回传的Intent
//                String str = b.getString("ListenB");//str即为回传的值"Hello, this is B speaking"
///* 得到B回传的数据后做什么... 略 */
//                break;
//            default:
//                break;
//        }
    }

    public void switch2MyPublishList() {
        FragmentMyPublishList fragment = new FragmentMyPublishList();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.content_frame, fragment );
        ft.addToBackStack( null );
        ft.commit();
    }

    public void switch2LoginFragment() {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        args.putString( "username", "wyf" );
        fragment.setArguments( args );
        //
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.content_frame, fragment );
        ft.addToBackStack(null);
        ft.commit();
    }

    public void switch2RegAccount() {
        FragmentRegAccount fragment = new FragmentRegAccount();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.content_frame, fragment );
        ft.addToBackStack( null );
        ft.commit();

    }

    public void switch2AttationUsers() {
        FragmentAttationUsers fragment = new FragmentAttationUsers();
        Bundle args = new Bundle();
        args.putString( "username", "wyf" );
        fragment.setArguments( args );

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.content_frame, fragment );
        ft.addToBackStack(null);
        ft.commit();
    }

    public void switch2Publish() {
        FragmentPublish fragment = new FragmentPublish();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.content_frame, fragment );
        ft.addToBackStack(null);
        ft.commit();
    }

    private void createNavigateHeader() {
        FragmentNavigateHeader fragment = new FragmentNavigateHeader();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.main_header, fragment );
        ft.commit();
    }

    private void createInfoPanel() {
        FragmentInfoPanel fragment = new FragmentInfoPanel();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace( R.id.left_drawer, fragment );
        ft.commit();
    }

    public void openDrawer() {
        DrawerLayout root = (DrawerLayout)findViewById( R.id.drawer_layout );
        if ( root == null ) {
            assert( false );
            return;
        }
        View leftDrawer = findViewById( R.id.left_drawer );
        if ( leftDrawer == null ) {
            assert( false );
            return;
        }

        root.openDrawer( leftDrawer );
    }

    public void closeDrawer() {
        DrawerLayout root = (DrawerLayout)findViewById( R.id.drawer_layout );
        if ( root == null ) {
            assert( false );
            return;
        }
        root.closeDrawers();
    }

}

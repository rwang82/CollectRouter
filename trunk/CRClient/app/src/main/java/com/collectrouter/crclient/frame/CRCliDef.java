package com.collectrouter.crclient.frame;

/**
 * Created by rom on 12/30 0030.
 */
public class CRCliDef {

    /////////////////////////////////////////////////////////////////////////////
    //
    //public final static String SERVER_IP_LOGIN = "192.168.31.106";
    //public final static String SERVER_IP_LOGIN = "192.168.123.1";
    //public final static String SERVER_IP_LOGIN = "10.0.2.10";
    public final static String SERVER_IP_LOGIN = "192.168.1.102";
    public final static int SERVER_PORT_LOGIN = 7654;

    /////////////////////////////////////////////////////////////////////////////
    //
    public final static int CRCLI_ACTIVITY_SOCKETTEST = 1;
    public final static int CRCLI_ACTIVITY_MAIN = 100;

    /////////////////////////////////////////////////////////////////////////////
    //
    public final static String CRCLI_FRAGMENT_LOGIN = "login";
    public final static String CRCLI_FRAGMENT_SHOW_ATTETION = "show_attetion";
    public final static String CRCLI_FRAGMENT_SHOW_ATTETIONED = "show_attetioned";
    public final static String CRCLI_FRAGMENT_SHOW_ACCOUNT_PRODUCT_LIST = "show_account_product_list";
    public final static String CRCLI_FRAGMENT_REGACCOUNT = "register_account";
    public final static String CRCLI_FRAGMENT_INFOPANEL = "info_panel";
    public final static String CRCLI_FRAGMENT_NAVIGATEHEADER = "navigate_header";
    public final static String CRCLI_FRAGMENT_DO_PUBLISH = "do_publish";
    public final static String CRCLI_FRAGMENT_DO_ATTETION = "do_attetion";
    public final static String CRCLI_FRAGMENT_MYPUBLISHLIST = "mypublish_list";


    /////////////////////////////////////////////////////////////////////////////
    //
    public final static int EOS_UNKNOWN = 0;
    public final static int EOS_WINDOWS = 100;
    public final static int EOS_MAC = 200;
    public final static int EOS_ANDROID = 300;
    public final static int EOS_IOS = 400;
    public final static int EOS_WINPHONE = 500;

    /////////////////////////////////////////////////////////////////////////////
    //
    public final static int CREVT_CONNECT_SUCCESS = 1;
    public final static int CREVT_CONNECT_FAILED = 2;
    public final static int CREVT_DISCONNECTED = 3;
    public final static int CREVT_RECVDATA = 4;

    public final static int CREVT_BTNCLICK_LOGIN = 20;
    public final static int CREVT_BTNCLICK_ACCOUNT_REG = 21;
    public final static int CREVT_BTNCLICK_ENTER_ATTATION = 22;
    public final static int CREVT_BTNCLICK_ENTER_PUBLISH = 23;
    public final static int CREVT_BTNCLICK_PHOTOGRAPH_4_PUBLISH = 24;
    public final static int CREVT_BTNCLICK_SELECTPICTURE_4_PUBLISH = 25;
    public final static int CREVT_BTNCLICK_SCAN_4_PUBLISH = 26;
    public final static int CREVT_BTNCLICK_COMMIT_PUBLISH = 27;


    public final static int CREVT_RECVRMSGJONS = 100;
    public final static int CREVT_RECVRMSGBINARY = 101;

    public final static int CREVT_START_LOGGING = 200;
    public final static int CREVT_LOGIN_SUCCESS = 201;
    public final static int CREVT_LOGIN_FAILED = 202;
    public final static int CREVT_REG_ACCOUNT_SUCCESS = 210;
    public final static int CREVT_RECV_PHOTOGRAPH_4_PUBLISH = 220;
    ///////////////////////////////
    // param1 - specify the uuid of product.
    // param2 - specify the product added to pending queue.
    public final static int CREVT_PRODUCT_PENDQUEUE_ADD = 230;
    ///////////////////////////////
    // param1 - specify the uuid of product.
    // param2 - NONE.
    public final static int CREVT_PRODUCT_PENDQUEUE_REMOVE = 231;
    ///////////////////////////////
    // param1 - specify the product added.
    // param2 - NONE.
    public final static int CREVT_PRODUCT_ADD = 232;
    ///////////////////////////////
    // param1 -
    // param2 -
    public final static int CREVT_PRODUCT_REMOVE = 233;
    ///////////////////////////////
    // param1 -
    // param2 -
    public final static int CREVT_CUR_LOGIN_ACCOUNT_INFO_UPDATE = 234;
    ///////////////////////////////
    // param1 - specify the nSN .
    // param2 - specify the CRAttetionListResult.
    public final static int CREVT_FETCH_ATTETION_LIST_SUCCESS = 235;
    ///////////////////////////////
    // param1 - specify the nSN.
    // param2 - specify the list of CRAccountData.
    public final static int CREVT_FETCH_ACCOUNT_DATA_SUCCESS = 236;
    ///////////////////////////////
    // param1 - specify the nSN.
    // param2 - specify the list of Account Name.
    public final static int CREVT_FETCH_ACCOUNT_PRODUCTS_SUCCESS = 237;
    ///////////////////////////////
    // param1 - specify the nSN.
    // param2 - specify the CRAttetionedListResult.
    public final static int CREVT_FETCH_ATTETIONED_LIST_SUCCESS = 238;







    /////////////////////////////////////////////////////////////////////////////
    //
    public final static int CRCMDSN_INVALID = -1;
//
    public final static int CRCMDTYPE_UNKNOWN = -1;
    public final static int CRCMDTYPE_REQ_LOGIN = 1;
    public final static int CRCMDTYPE_ACK_LOGIN = 2;
    public final static int CRCMDTYPE_REQ_LOGOFF = 3;
    public final static int CRCMDTYPE_ACK_LOGOFF = 4;
    public final static int CRCMDTYPE_REQ_ACCOUNT_REG = 5;
    public final static int CRCMDTYPE_ACK_ACCOUNT_REG = 6;
    public final static int CRCMDTYPE_REQ_PRODUCT_PUBLISH = 11;
    public final static int CRCMDTYPE_ACK_PRODUCT_PUBLISH = 12;
    public final static int CRCMDTYPE_REQ_ADD_ATTETION = 13;
    public final static int CRCMDTYPE_ACK_ADD_ATTETION = 14;
    public final static int CRCMDTYPE_REQ_FETCH_ATTETION_LIST = 15;
    public final static int CRCMDTYPE_ACK_FETCH_ATTETION_LIST = 16;
    public final static int CRCMDTYPE_REQ_FETCH_ACCOUNTINFO = 17;
    public final static int CRCMDTYPE_ACK_FETCH_ACCOUNTINFO = 18;
    public final static int CRCMDTYPE_REQ_FETCH_ACCOUNTPRODUCTS = 19;
    public final static int CRCMDTYPE_ACK_FETCH_ACCOUNTPRODUCTS = 20;
    public final static int CRCMDTYPE_REQ_FETCH_ATTETIONED_LIST = 21;
    public final static int CRCMDTYPE_ACK_FETCH_ATTETIONED_LIST = 22;

    //
    public final static int CRREQUESTCODE_PHTOGRAPH4PUBLISH = 1000;


    public final static String CRSTR_NOT_LOGGED_IN = "Not Logged In";
    public final static String CRSTR_START_LOGGING = "Logging ...";
}

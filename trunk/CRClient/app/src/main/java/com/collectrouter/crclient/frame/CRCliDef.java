package com.collectrouter.crclient.frame;

/**
 * Created by rom on 12/30 0030.
 */
public class CRCliDef {
    //
    public final static String SERVER_IP_LOGIN = "192.168.31.106";
    public final static int SERVER_PORT_LOGIN = 7654;
    //
    public final static int CRCLI_ACTIVITY_SOCKETTEST = 1;
    public final static int CRCLI_ACTIVITY_MAIN = 100;
    public final static int CRCLI_ACTIVITY_REGACCOUNT =101;
    //
    public final static int CRCLI_FRAGMENT_LOGIN = 100;
    public final static int CRCLI_FRAGMENT_ATTATION_USERS = 101;
    //
    public final static int EOS_UNKNOWN = 0;
    public final static int EOS_WINDOWS = 100;
    public final static int EOS_MAC = 200;
    public final static int EOS_ANDROID = 300;
    public final static int EOS_IOS = 400;
    public final static int EOS_WINPHONE = 500;

    //
    public final static int CREVT_CONNECT_SUCCESS = 1;
    public final static int CREVT_CONNECT_FAILED = 2;
    public final static int CREVT_DISCONNECTED = 3;
    public final static int CREVT_RECVDATA = 4;

    public final static int CREVT_BTNCLICK_LOGIN = 20;
    public final static int CREVT_BTNCLICK_ACCOUNT_REG = 21;
    public final static int CREVT_BTNCLICK_ATTATION = 22;
    public final static int CREVT_BTNCLICK_PUBLISH = 23;

    public final static int CREVT_RECVRMSGJONS = 100;
    public final static int CREVT_RECVRMSGBINARY = 101;


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
}

package com.collectrouter.nwe;

/**
 * Created by rom on 12/17 0017.
 */
public class HMNWECliDef {
    //
    public final static int BUFSIZE_RECV_NWDATA = 1024;

    /////////////////////////////////////////////////
    public enum ENUMSTATUSTYPE {
        ESTATUS_DISCONNECTED,
        ESTATUS_CONNECTING,
        ESTATUS_CONNECTED,
        ESTATUS_DISCONNECTING,
    }

    /////////////////////////////////////////////////
    // MSG_REQ_CONNECT
    // TITLE_CONNECT_IP:  specify the ip address.
    // TITLE_CONNECT_PORT:  specify the port of server.
    public final static String TITLE_CONNECT_IP = "ipaddr";
    public final static String TITLE_CONNECT_PORT = "port";
    public final static int MSG_REQ_CONNECT = 1;

    /////////////////////////////////////////////////
    // MSG_REQ_CLOSECONNECT
    // key           value
    public final static int MSG_REQ_CLOSECONNECT = 2;

    /////////////////////////////////////////////////
    // MSG_REQ_SENDDATA
    // key                        value
    // TITLE_SENDDATA_RAWDATA:    specify the raw buffer need send.
    // TITLE_SNEDDATA_LENRAWDATA: specify the length of raw buffer need send.
    public final static String TITLE_SENDDATA_RAWDATA = "rawdata";
    public final static String TITLE_SENDDATA_LENRAWDATA = "len";
    public final static int MSG_REQ_SENDDATA = 3;

    /////////////////////////////////////////////////
    // MSG_ACK_CONNECT
    // key                        value
    // TITLE_CONNECT_IP           specify the ip address.
    // TITLE_CONNECT_PORT         specify the port of server.
    // TITLE_CONNECT_RESULT       specify if connect success.
    public final static String TITLE_CONNECT_RESULT = "result";
    public final static int MSG_ACK_CONNECT = 4;

    /////////////////////////////////////////////////
    // MSG_NOTIFY_DISCONNECTED
    //
    //
    public final static int MSG_NOTIFY_DISCONNECTED = 5;


    /////////////////////////////////////////////////
    // MSG_NOTIFY_RECVDATA
    //
    //
    public final static String TITLE_RECVDATA_BUF = "buf";
    public final static String TITLE_RECVDATA_LENBUF = "len";
    public final static int MSG_NOTIFY_RECVDATA = 6;


}

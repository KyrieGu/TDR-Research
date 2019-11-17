package tdr.filter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/*
   This class is a Thread object that is used to control the connection with the SIS server.
 */
class ComponentSocket extends Thread {
    //private static final String TAG = "ComponentSocket";
    private static String serverAddress;
    private static int serverPort;
    private static MsgEncoder msgEncoder;
    private static MsgDecoder msgDecoder;
    private static KeyValueList record = new KeyValueList();
    private static int signalLength=10000;//Duncan add [Feb 2017] 100 means 100 records
    private static int samplingRate=4;//Duncan add [Feb 2017] 10 means to get data every 10 second
    private static long startTime = System.currentTimeMillis();
    private static long time = System.currentTimeMillis();

    Socket socket;
    KeyValueList message;

    //Callback object that is used to update UI(Activity) as long as there is something
    // happen to the connection between the component and SIS server.
    Handler callback;

    //Control flag variable that determines whether the thread should be alive
    boolean killThread = false;

    ComponentSocket() {
    }
    //TODO Socket communication always requires the receiver replies to the sender back.
    ComponentSocket(String addr, int port, Handler callbacks) {
        serverAddress = addr;
        serverPort = port;
        Log.d(MainActivity.TAG, "Server Address: " + serverAddress);
        Log.d(MainActivity.TAG, "Server Port: " + serverPort);
        callback = callbacks;
    }

    @Override
    public void run() {
        super.run();
        //Keep listening if there is any incoming messages
        while(!killThread){
            try {
                //Build a new socket
                socket = new Socket(serverAddress, serverPort);
                socket.setKeepAlive(true);
                msgDecoder = new MsgDecoder(socket.getInputStream());
                msgEncoder = new MsgEncoder(socket.getOutputStream());
                //Tell the activity that a new socket has been built.
                Message message = callback.obtainMessage(MainActivity.CONNECTED);
                callback.sendMessage(message);
                killThread = false;
                while(true){
                    //Check if there is an incoming message.
                    KeyValueList kvList = msgDecoder.getMsg();
                    String messageType = kvList.getValue("MessageType");
                    String receiver = kvList.getValue("Receiver");
//                    Log.e(MainActivity.TAG, "Received raw: <" + kvList.encodedString() + ">");

                    switch (messageType) {
                        case "Confirm":
                            System.out.println("Connect to SISServer successful.");
                            break;
                        case "Reading"://Liang	According BCISensor's send message
                            Log.e(MainActivity.TAG, "Received raw: <" + kvList.encodedString() + ">");
                            //Tell the activity that a new message has been received.
                            String _samplingRate = kvList.getValue("SamplingRate");
                            String _signalLength = kvList.getValue("SignalLength");
                            if(!_samplingRate.equals("") || !_signalLength.equals("")) {
                                Log.e(MainActivity.TAG, "Received raw: <" + kvList.encodedString() + ">");
                                if (!_samplingRate.equals(""))
                                    samplingRate = Integer.parseInt(_samplingRate);
                                if (!_signalLength.equals(""))
                                    signalLength = Integer.parseInt(_signalLength);
                            }else {
                                Log.e(MainActivity.TAG, "Received raw: <" + kvList.encodedString() + ">");

                                record = kvList;
                                filterBCI();
                            }
                            Message msg = callback.obtainMessage(MainActivity.MESSAGE_RECEIVED);
                            msg.obj = kvList.toString();
                            callback.sendMessage(msg);
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message message = callback.obtainMessage(MainActivity.DISCONNECTED);
                callback.sendMessage(message);
            }
            try {
                Thread.sleep(100);
            }  catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void filterBCI() {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - time;
        if((diffTime>(1000*samplingRate)) && ((currentTime - startTime) < (signalLength*1000))) {
            try {
                System.out.println("BCIFilter:filterBCI");
//        	record.putPair("Receiver", "BCIUploader");
                record.putPair("Receiver", "Uploader");//for android uploader
                record.putPair("Sender", "BCIFilter");
                record.putPair("Purpose", "upload");
                msgEncoder.sendMsg(record);
                time = System.currentTimeMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Kill the socket listening thread by setting the alive flag to true
    void killThread() {
        killThread = true;
        Message message = callback.obtainMessage(MainActivity.DISCONNECTED);
        callback.sendMessage(message);
        Log.e(MainActivity.TAG, "Sock thread killed." );
    }
    //The function is called by the activity and used to set the output message
    void setMessage(KeyValueList kvList) {
        if(msgEncoder!=null){
            try {
                Log.e(MainActivity.TAG, "Sending messsage." );
                msgEncoder.sendMsg(kvList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    boolean isSocketAlive(){
        if(socket==null)
            return false;
        return socket.isConnected() ;

    }

    //Just send an acknowledgement if there is nothing to be sent at the moment
    void ack(){
        KeyValueList reply = new KeyValueList();
        reply.putPair("ack","ack");
        try {
            msgEncoder.sendMsg(reply);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "IOException: " + e.toString());
        }
    }
}
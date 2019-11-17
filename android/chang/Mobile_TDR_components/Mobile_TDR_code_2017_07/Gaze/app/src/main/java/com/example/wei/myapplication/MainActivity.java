package com.example.wei.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/*
    A demo Activity, which shows how to build a connection with SIS server, register itself to the server,
    and send a message to the server as well.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Input Processor";


    public double finalResult=0;
    private static Button connectToServerButton,registerToServerButton
            ,collectDataButton,sendMessageButton;

    private EditText serverIp,serverPort;

    //A Thread object that used to control the connection with the SIS server.
    static ComponentSocket client;
    static TextView tv;

    private static TextView messageReceivedListText;

    private static final String SENDER = "Gaze";//change to mental state@@@@@@@@@@@@@@@@@
    public static int durationVar=5;


    private static final String REGISTERED = "Registered";
    private static final String DISCOONECTED =  "Disconnect";
    private static final String SCOPE = "SIS.Scope1";

    private KeyValueList readingMessage;
    private KeyValueList readingMessage2;

    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    public static final int MESSAGE_RECEIVED = 3;

    //The object is passed to the socket thread and used as callbacks to update UI.
    static Handler callbacks = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String str;
            String[] strs;
            switch (msg.what) {
                case CONNECTED:
                    registerToServerButton.setText(REGISTERED);
                    Log.e(TAG, "===============================================================CONNECTED" );
                    break;
                case DISCONNECTED:
                    connectToServerButton.setText("Connect");
                    Log.e(TAG, "===============================================================DISCONNECTED" );
                    break;
                case MESSAGE_RECEIVED:
                    str = (String)msg.obj;
                    if(str!="")
                        durationVar = Integer.parseInt(str);
                    tv.setText(str);
                    messageReceivedListText.append(str + "********************\n");
                    final int scrollAmount = messageReceivedListText.getLayout().getLineTop(messageReceivedListText.getLineCount()) - messageReceivedListText.getHeight();
                    if (scrollAmount > 0)
                        messageReceivedListText.scrollTo(0, scrollAmount);
                    else
                        messageReceivedListText.scrollTo(0, 0);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_main);
  //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        tv = (TextView)findViewById(R.id.textViewResult);

        connectToServerButton = (Button) findViewById(R.id.connectToServer);
        registerToServerButton = (Button) findViewById(R.id.registerToServerButton);
        collectDataButton = (Button) findViewById(R.id.collectDataButton);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        serverIp = (EditText) findViewById(R.id.serverIp);
        serverPort = (EditText) findViewById(R.id.serverPort);
        messageReceivedListText = (TextView) findViewById(R.id.messageReceivedListText);
        //Pass a scrolling movement manager to the text view so that this view can be scrolled
        messageReceivedListText.setMovementMethod(ScrollingMovementMethod.getInstance());

        registerToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(client!=null && client.isSocketAlive() && registerToServerButton.getText().toString().equalsIgnoreCase(REGISTERED)){
                    Toast.makeText(MainActivity.this,"Already registered.",Toast.LENGTH_SHORT).show();
                }else{
                    client = new ComponentSocket(serverIp.getText().toString(), Integer.parseInt(serverPort.getText().toString()),callbacks);
                    client.start();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            KeyValueList list = generateRegisterMessage();
                            client.setMessage(list);
                        }
                    }, 100);

                }
            }
        });
        connectToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(MainActivity.TAG, "Sending connectToServerButton.1" );
                if(connectToServerButton.getText().toString().equalsIgnoreCase(DISCOONECTED)){
                    Log.e(MainActivity.TAG, "Sending connectToServerButton.2" );
                    client.killThread();
                    connectToServerButton.setText("Connect");
                }else{
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            KeyValueList list = generateConnectMessage();
                            client.setMessage(list);
                        }
                    }, 100);

                    connectToServerButton.setText(DISCOONECTED);
                }
            }
        });
        collectDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readingMessage = generateReadingMessage();
                readingMessage2 = generateReadingMessage2();
            }
        });
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(client!=null && client.isSocketAlive() && readingMessage.size()>0){
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            client.setMessage(readingMessage);
                            client.setMessage(readingMessage2);
                        }
                    }, 100);

                }else{
                    Toast.makeText(MainActivity.this,"Please generate a message first.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Generate a test register message, please replace something of attributes with your own.
    KeyValueList generateRegisterMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope", SCOPE);
        //Set the message type
        list.putPair("MessageType", "Register");
        //Set the sender or name of the message
        list.putPair("Sender", SENDER);
        //Set the role of the message
        list.putPair("Role", "Basic");
        //Set the name of the component
        list.putPair("Name", SENDER);
        return list;
    }
    //Generate a test connect message, please replace something of attributes with your own.
    KeyValueList generateConnectMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Connect");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");
        //Set the name of the component
        list.putPair("Name",SENDER);
        return list;
    }
    //Generate a test register message, please replace something of attributes with your own.
    KeyValueList generateReadingMessage(){ ////send message to server @@@@@@@@@@@@@@@@@
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Reading");
        //list.putPair("XXXXX","??"); @@@@@@@@@@@@@@
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
//        list.putPair("Role","Basic");

        //the following three attributes are necessary for sending the message to Uploader through PC SIS server.
//        list.putPair("Broadcast", "True");
//        list.putPair("Direction", "Up");
        list.putPair("Receiver", "PrjRemote");

//        list.putPair("Data_BP", "unavailable");

        //Send to GUI
        list.putPair("Gaze", ""+finalResult);


//        String data = "EMG:333ECG:111V";
//        if(data.contains("EMG:") && data.contains("ECG:")){
//            int index = data.indexOf("EMG:");
//            int ecgindex = data.indexOf("ECG:");
//            int vindex = data.indexOf("V");
//            System.out.println("index:"+index);
//            System.out.println("ecgindex:"+ecgindex);
//            String emg = data.substring(index+4, ecgindex);
//            String ecg = data.substring(ecgindex+4, vindex);
//            System.out.println("emg:"+emg);
//            System.out.println("ecg:"+ecg);
//
//            list.putPair("Data_EMG", emg);
//            list.putPair("Data_ECG", ecg);
//        }
//
//        list.putPair("Data_Pulse", "unavailable");

//        long curr_time = System.currentTimeMillis();
//        list.putPair("Data_Date", String.valueOf(curr_time));
        return list;
    }

    KeyValueList generateReadingMessage2(){ ////send message to server @@@@@@@@@@@@@@@@@
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Reading");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);

        list.putPair("Receiver", "Uploader");

        //Send to Uploader
        list.putPair("Value", ""+finalResult);
        list.putPair("Purpose", "upload");

        return list;
    }

    public void onClickToSetDuration(View view) {
        Intent i = new Intent(getApplicationContext(), mentalState.class);
        i.putExtra("duration", durationVar);
        //startActivity(i);
        startActivityForResult (i, 331);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 331 && resultCode == Activity.RESULT_OK && data != null) {
            finalResult = data.getDoubleExtra("fuzzy",0);
            tv.setText(""+finalResult);
        }
    }


}

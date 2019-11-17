package tdr.audio;

import android.media.MediaPlayer;
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

    static MediaPlayer Sound1;
    static MediaPlayer Sound2;
    static MediaPlayer Sound3;;


    public static final String TAG = "Audio";

    private static Button connectToServerButton,registerToServerButton;

    private EditText serverIp,serverPort;

    static ComponentSocket client;

    private static TextView messageReceivedListText;

    private static final String SENDER = "Audio";
    private static final String REGISTERED = "Registered";
    private static final String DISCOONECTED =  "Disconnect";
    private static final String SCOPE = "SIS.Scope1";

    private KeyValueList readingMessage;

    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    public static final int MESSAGE_RECEIVED = 3;

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
                    str = (String)msg.obj;//str is the SoundNum in ComponentSocket
                    messageReceivedListText.append(str+"********************\n");
                    final int scrollAmount = messageReceivedListText.getLayout().getLineTop(messageReceivedListText.getLineCount()) - messageReceivedListText.getHeight();
                    if (scrollAmount > 0)
                        messageReceivedListText.scrollTo(0, scrollAmount);
                    else
                        messageReceivedListText.scrollTo(0, 0);

                    if(str.equals("1")||str.equals("2")||str.equals("3"))
                        PlaySound(Integer.parseInt(str));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    protected void onPause(){
        super.onPause();
        if(Sound1.isPlaying()) {
            Sound1.stop();
//            Sound1.reset();
        }
        if(Sound2.isPlaying()) {
            Sound2.stop();
//            Sound2.reset();
        }
        if(Sound3.isPlaying()) {
            Sound3.stop();
        }
    }

    static void PlaySound(int soundNum){
        if(Sound1.isPlaying()) {
            Sound1.stop();
//            Sound1.reset();
        }
        if(Sound2.isPlaying()) {
            Sound2.stop();
//            Sound2.reset();
        }
        if(Sound3.isPlaying()) {
            Sound3.stop();
//            Sound3.reset();
        }


        if(soundNum==1) {
            Sound1.start();
        }else if(soundNum==2){
            Sound2.start();
        }else if(soundNum==3){
            Sound3.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Sound1 = MediaPlayer.create(this, R.raw.sleep);
        Sound2 = MediaPlayer.create(this, R.raw.sleep2);
        Sound3 = MediaPlayer.create(this, R.raw.sleep3);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        connectToServerButton = (Button) findViewById(R.id.connectToServer);
        registerToServerButton = (Button) findViewById(R.id.registerToServerButton);
        serverIp = (EditText) findViewById(R.id.serverIp);
        serverPort = (EditText) findViewById(R.id.serverPort);
        messageReceivedListText = (TextView) findViewById(R.id.messageReceivedListText);
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
                    }, 500);
                }
            }
        });
        connectToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }
/*
    public static void doDuncan(){
    }

    public static void doDuncan2(){
        client.setView();
    }
*/
    //Generate a test register message, please replace something of attributes with your own.
    KeyValueList generateRegisterMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Register");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");
        list.putPair("Name",SENDER);
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
}

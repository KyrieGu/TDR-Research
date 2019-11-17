import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CreateReadingBehaviorController
{

    // socket for connection to SISServer
    static Socket universal;
    private static int port = 53217;
    // message writer
    static MsgEncoder encoder;
    // message reader
    static MsgDecoder decoder;
    // scope of this component
    private static final String SCOPE = "SIS.Ren";
	// name of this component
    private static final String NAME = "ReadingBehaviorController";
    // messages types that can be handled by this component
    private static final List<String> TYPES = new ArrayList<String>(
        Arrays.asList(new String[] { "Setting", "Confirm", "Reading", "Alert"}));

    //private static int refreshRate = 500, max = 40, min = 15;
    private static Date startDate = new Date(), endDate = new Date();

    private static Timer timer = new Timer();

	private static StateMachine stateMachine;

    /*
     * Main program
     */
    public static void main(String[] args)
    {
        while (true)
        {
            try
            {

                // try to establish a connection to SISServer
                universal = connect();

                // bind the message reader to inputstream of the socket
                decoder = new MsgDecoder(universal.getInputStream());
                // bind the message writer to outputstream of the socket
                encoder = new MsgEncoder(universal.getOutputStream());
				
				//state machine
				stateMachine = new StateMachine(NAME, SCOPE, encoder);
                /*
                 * construct a Connect message to establish the connection
                 */
                KeyValueList conn = new KeyValueList();
                conn.putPair("Scope", SCOPE);
                conn.putPair("MessageType", "Connect");
				conn.putPair("Role", "Basic");
                conn.putPair("Name", NAME);
                encoder.sendMsg(conn);

                initRecord();

                // KeyValueList for inward messages, see KeyValueList for
                // details
                KeyValueList kvList;

                while (true)
                {
                    // attempt to read and decode a message, see MsgDecoder for
                    // details
                    kvList = decoder.getMsg();

                    // process that message
                    ProcessMsg(kvList);
                }

            }
            catch (Exception e)
            {
                // if anything goes wrong, try to re-establish the connection
                e.printStackTrace();
                try
                {
                    // wait for 1 second to retry
                    Thread.sleep(1000);
                }
                catch (InterruptedException e2)
                {
                }
                System.out.println("Try to reconnect");
                try
                {
                    universal = connect();
                }
                catch (IOException e1)
                {
                }
            }
        }
    }

    /*
     * used for connect(reconnect) to SISServer
     */
    static Socket connect() throws IOException
    {
        Socket socket = new Socket("127.0.0.1", port);
        return socket;
    }

    private static void initRecord()
    {

    }

    private static void ProcessMsg(KeyValueList kvList) throws Exception
    {

        String scope = kvList.getValue("Scope");
        if (!SCOPE.startsWith(scope))
        {
            return;
        }

        String messageType = kvList.getValue("MessageType");
        if (!TYPES.contains(messageType))
        {
            return;
        }

        String sender = kvList.getValue("Sender");

        String receiver = kvList.getValue("Receiver");

        String purpose = kvList.getValue("Purpose");

        switch (messageType)
        {
        case "Confirm":
            System.out.println("Connect to SISServer successful.");
            break;
        // Behaviour
		case "Alert":
			switch (sender) {
			case "ReadingBehavior":
				break;
			}
		}
        // State
        stateMachine.changeState(messageType, purpose);
    }

}

class TempReading
{
    double temp;
    long date;
}

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CreateInputProcessor
{

    // socket for connection to SISServer
    private static Socket universal;
    private static int port = 53217;
    // message writer
    static MsgEncoder encoder;
    // message reader
    static MsgDecoder decoder;

    // scope of this component
    private static final String SCOPE = "SIS.Ren";
	// name of this component
    private static final String NAME = "InputProcessor";
    // messages types that can be handled by this component
    private static final List<String> TYPES = new ArrayList<String>(
        Arrays.asList(new String[] { "Setting", "Confirm" }));

    private static int refreshRate = 500, max, min;
    private static Date startDate = new Date(), endDate = new Date();

    private static Timer timer = new Timer();

    static KeyValueList recordBP = new KeyValueList();
    static KeyValueList recordSPO2 = new KeyValueList();
    static KeyValueList recordEKG = new KeyValueList();

    private static NewSerialReader reader;

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
        recordBP.putPair("Scope", SCOPE);
        recordBP.putPair("MessageType", "Setting");
        recordBP.putPair("Sender", NAME);
        recordBP.putPair("Receiver", "BloodPressure");
        recordBP.putPair("Purpose", "UpdateRecord");

        recordSPO2.putPair("Scope", SCOPE);
        recordSPO2.putPair("MessageType", "Setting");
        recordSPO2.putPair("Sender", NAME);
        recordSPO2.putPair("Receiver", "SPO2");
        recordSPO2.putPair("Purpose", "UpdateRecord");

        recordEKG.putPair("Scope", SCOPE);
        recordEKG.putPair("MessageType", "Setting");
        recordEKG.putPair("Sender", NAME);
        recordEKG.putPair("Receiver", "EKG");
        recordEKG.putPair("Purpose", "UpdateRecord");
    }

    private static void componentTask()
    {
        try
        {
            long now = System.currentTimeMillis();
            recordBP.putPair("Systolic", reader.reading.getSystolic() + "");
            recordBP.putPair("Diastolic", reader.reading.getDiastolic() + "");
            recordBP.putPair("Pulse", reader.reading.getPulse() + "");
            recordBP.putPair("Date", now + "");
            System.out.println(recordBP);
            encoder.sendMsg(recordBP);

            recordSPO2.putPair("SPO2", reader.reading.getSPO2() + "");
            recordSPO2.putPair("Date", now + "");
            System.out.println(recordSPO2);
            encoder.sendMsg(recordSPO2);

            //recordEKG.putPair("EKG", reader.reading.ekg + "");
            recordEKG.putPair("LeadISeq", reader.reading.getLeadISeq());
            recordEKG.putPair("LeadIISeq", reader.reading.getLeadIISeq());
            recordEKG.putPair("LeadIIISeq", reader.reading.getLeadIIISeq());
            recordEKG.putPair("Date", now + "");
            System.out.println(recordEKG);
            encoder.sendMsg(recordEKG);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static void ProcessMsg(KeyValueList kvList) throws Exception
    {
        System.out.println(kvList);
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
        case "Setting":
            if (receiver.equals(NAME))
            {
                System.out.println("Message from " + sender);
                System.out.println("Message type: " + messageType);
                System.out.println("Message Purpose: " + purpose);
                switch (purpose)
                {

                case "Activate":
                    String rRate = kvList.getValue("RefreshRate");
                    String sDate = kvList.getValue("StartDate");
                    String eDate = kvList.getValue("EndDate");
                    String maxx = kvList.getValue("Max");
                    String minn = kvList.getValue("Min");

                    if (rRate != null && !rRate.equals(""))
                    {

                        refreshRate = Integer.parseInt(rRate);

                    }

                    if (sDate != null && !sDate.equals("") && eDate != null
                            && !eDate.equals(""))
                    {
                        startDate.setTime(Long.parseLong(sDate));
                        endDate.setTime(Long.parseLong(eDate));
                    }

                    if (maxx != null && !maxx.equals("") && minn != null
                            && !minn.equals(""))
                    {
                        max = Integer.parseInt(maxx);
                        min = Integer.parseInt(minn);
                    }

                    try
                    {
                        timer.cancel();
                        timer = new Timer();
                    }
                    catch (Exception e)
                    {
                        // TODO: handle exception
                    }
                    reader = new NewSerialReader();
                    timer.schedule(new TimerTask()
                    {

                        @Override
                        public void run()
                        {
                            // TODO Auto-generated method stub
                            if (System.currentTimeMillis() - endDate.getTime() > 0)
                            {
                                cancel();
                            }
                            else
                            {
                                componentTask();
                            }
                        }
                    }, startDate, refreshRate);
                    System.out.println("Algorithm Activated");
                    break;

                case "Kill":
                    if(reader != null)
                    {
                        reader.close();
                    }
                    try {
                        timer.cancel();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    System.exit(0);
                    break;
                // case "Activate":
                // try {
                // timer.cancel();
                // timer = new Timer();
                // } catch (Exception e) {
                // // TODO: handle exception
                // }
                // reader.start();
                // timer.schedule(new TimerTask() {
                //
                // @Override
                // public void run() {
                // // TODO Auto-generated method stub
                // if (System.currentTimeMillis() - endDate.getTime() > 0) {
                // cancel();
                // } else {
                // componentTask();
                // }
                // }
                // }, startDate, refreshRate);
                // System.out.println("Algorithm Activated");
                // break;
                case "Deactivate":
                    try
                    {
                        timer.cancel();
                    }
                    catch (Exception e)
                    {
                        // TODO: handle exception
                    }
                    if(reader != null)
                    {
                        reader.close();
                    }
                    System.out.println("Algorithm Deactivated");
                    break;
                }
            }
            break;
        }
    }
}

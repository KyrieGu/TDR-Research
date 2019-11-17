import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Calendar;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class CreateReadingBehaviorUploader
{
    // socket for connection to SISServer
    static Socket universal;
    // message writer
    static MsgEncoder encoder;
    // message reader
    static MsgDecoder decoder;

    // scope of this component
    private static final String SCOPE = "SIS.Ren";
	// name of this component
    private static final String NAME = "ReadingBehaviorUploader";
    // messages types that can be handled by this component
    private static final List<String> TYPES = new ArrayList<String>(
        Arrays.asList(new String[] { "Alert", "Emergency", "Confirm", "Setting" }));
		
	private static final String incomingMessages = "IN\tConfirm|Setting:SwitchUser|Setting:UpdateRecipients|Setting:ForceUpload|Setting:Kill|Alert|Emergency\n";
    private static final String outgoingMessages = "OUT\tConnect";

    private static UploaderReading reading = new UploaderReading();

    // variables for sending emails
    static final String SMTP_HOST_NAME = "smtp.ksiresearch.org.ipage.com";
    static final String SMTP_PORT = "587";
    static final String emailMsgTxt = "Test Message Contents";
    static final String emailSubjectTxt = "Personal Healthcare Data From SIS System."; // title
    static final String emailFromAddress = "chronobot@ksiresearch.org";
    static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

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
				conn.putPair("IncomingMessages", incomingMessages);
                conn.putPair("OutgoingMessages", outgoingMessages);
                conn.putPair("Role", "Advertiser");
                conn.putPair("Name", NAME);
                encoder.sendMsg(conn);

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
        Socket socket = new Socket("127.0.0.1", 53217);
        return socket;
    }

    /*
     * Method for sending email which contains the information which GUI shows
     * on screen.
     */
    static void sendSSLMessage(String from, String recipients[],
                               String subject, String message) throws MessagingException
    {
        boolean debug = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "true");

        Session session = Session.getDefaultInstance(props,
                          new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(
                           "chronobot@ksiresearch.org", "Health14");
            }
        });
        session.setDebug(debug);

        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++)
        {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        {
            Multipart multipart = new MimeMultipart("related");
            {
                Multipart newMultipart = new MimeMultipart("alternative");
                BodyPart nestedPart = new MimeBodyPart();
                nestedPart.setContent(newMultipart);
                multipart.addBodyPart(nestedPart);
                {
                    BodyPart part = new MimeBodyPart();
                    part.setText("SIS DATA:");
                    newMultipart.addBodyPart(part);

                    part = new MimeBodyPart();
                    // the first string is email context

                    /*part.setContent(
                        "Here is the current status of the patient "
                        + "(This is an automatic massage send from SIS system): "
                        + "<br>LastName: "
                        + reading.lastName
                        + "<br>FirstName: "
                        + reading.firstName
                        + "<br>SPO2: "
                        + reading.spo2
                        + "<br>Systolic: "
                        + reading.systolic
                        + "<br>Diastolic: "
                        + reading.diastolic
                        + "<br>Pulse: "
                        + reading.pulse
                        + "<br>Date of Blood Pressure: "
                        + reading.dateBP
						+ "<br>SPO2: "
                        + reading.spo2
                        + "<br>Date of SPO2: "
                        + reading.dateSPO2
                        + "<br>EKG: "
                        + reading.ekg
                        + "<br>Date of EKG: "
                        + reading.dateEKG
                        + "<br>Temperature: "
                        + reading.temp
                        + "<br>Date of Temperature: "
                        + reading.dateTemp
                        + "<br>Kinect Status: "
                        + reading.kinectStatus
                        + "<br>Date of Kinect: "
                        + reading.dateKinect, "text/html");*/
                    newMultipart.addBodyPart(part);
                }
            }
            /*
             * BodyPart part = new MimeBodyPart();
             * part.setText(
             * "Here is the SPO2 and Blood Pressure data(This is an automatic massage send from SIS system): LastName: "
             * + lname
             * + " FirstName: "
             * + fname
             * + "\nSPO2: "
             * + spo2
             * + " "
             * + "\nSystolic: "
             * + systolic + "\nDiastolic: " + diastolic + "\nPulse: " + pulse);
             * multipart.addBodyPart(part);
             */
            msg.setContent(multipart);

        }
        Transport.send(msg);
        System.out.println("Successfully Sent mail to All Users, lol.\n");
    }

    /*
     * Method for sending email for Alert Message
     */
    static void sendAlertSSLMessage(String from, String recipients[],
                                    String subject, String message) throws MessagingException
    {
        boolean debug = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "true");

        Session session = Session.getDefaultInstance(props,
                          new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(
                           "chronobot@ksiresearch.org", "Health14");
            }
        });
        session.setDebug(debug);

        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++)
        {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        {
            Multipart multipart = new MimeMultipart("related");
            {
                Multipart newMultipart = new MimeMultipart("alternative");
                BodyPart nestedPart = new MimeBodyPart();
                nestedPart.setContent(newMultipart);
                multipart.addBodyPart(nestedPart);
                {
                    BodyPart part = new MimeBodyPart();
                    part.setText("SIS Alert!");
                    newMultipart.addBodyPart(part);

                }
            }
            BodyPart part = new MimeBodyPart();
            part.setText(message);
            multipart.addBodyPart(part);
            msg.setContent(multipart);
        }
        Transport.send(msg);
        System.out.println("Successfully Sent mail to All Users. :)\n");
    }

    // ============= end of sending email ====================

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

        case "Alert":
            System.out.println("\n*** Alert from "+sender+" ***");
            switch (sender)
            {
  /*          case "BloodPressure":
                
                String sys = kvList.getValue("Systolic");
                String dia = kvList.getValue("Diastolic");
                String pul = kvList.getValue("Pulse");
                String datBP = kvList.getValue("Date");

                if (sys != null && !sys.equals(""))
                {
                    reading.systolic = Integer.parseInt(sys);
                }
                if (dia != null && !dia.equals(""))
                {
                    reading.diastolic = Integer.parseInt(dia);
                }
                if (pul != null && !pul.equals(""))
                {
                    reading.pulse = Integer.parseInt(pul);
                }
                if (datBP != null && !datBP.equals(""))
                {
                    reading.dateBP = Long.parseLong(datBP);
                }
                break;
            case "EKG":

                break;
            case "SPO2":
                String spo = kvList.getValue("SPO2");
                String datSP = kvList.getValue("Date");

                if (spo != null && !spo.equals(""))
                {
                    reading.spo2 = Integer.parseInt(spo);
                }
                if (datSP != null && !datSP.equals(""))
                {
                    reading.dateSPO2 = Long.parseLong(datSP);
                }
                break;
            case "Temp":
                String tem = kvList.getValue("Temp");
                String datTe = kvList.getValue("Date");

                if (tem != null && !tem.equals(""))
                {
                    reading.temp = Double.parseDouble(tem);
                }
                if (datTe != null && !datTe.equals(""))
                {
                    reading.dateTemp = Long.parseLong(datTe);
                }
                break;
            case "KinectMonitor":
                String sta = kvList.getValue("Status");
                String datKi = kvList.getValue("Date");

                if (sta != null && !sta.equals(""))
                {
                    reading.kinectStatus = sta;
                }
                if (datKi != null && !datKi.equals(""))
                {
                    reading.dateKinect = Long.parseLong(datKi);
                }
                break;*/
				
			case "ReadingBehavior":
				String x=kvList.getValue("GazeX");
				String y=kvList.getValue("GazeY");
				String t=kvList.getValue("Timestamp");
				
				String xArrayString = kvList.getValue("GazeXArray");
				String yArrayString = kvList.getValue("GazeYArray");
				String tArrayString = kvList.getValue("TimeStampArray");
				

				System.out.println(xArrayString);
				System.out.println(yArrayString);
				System.out.println(tArrayString);
				
				List<String> listx = new ArrayList<String>(Arrays.asList(xArrayString.split(",")));
				List<String> listy = new ArrayList<String>(Arrays.asList(yArrayString.split(",")));
				List<String> listt = new ArrayList<String>(Arrays.asList(tArrayString.split(",")));
				
				
                if (x != null && !x.equals(""))
                {
                    reading.x = Double.parseDouble(x);
                }
                if (y != null && !y.equals(""))
                {
                    reading.y = Double.parseDouble(y);
                }
				if (t != null && !t.equals(""))
                {
                    reading.t = Long.parseLong(t);
                }
				for(int tempi = 0;tempi<listx.size();tempi++){
					String tempString = listx.get(tempi);
					if (tempString != null && !tempString.equals(""))
	                {
	                    reading.xArray.add(Double.parseDouble(tempString));
	                }
				}
				for(int tempi = 0;tempi<listy.size();tempi++){
					String tempString = listy.get(tempi);
					if (tempString != null && !tempString.equals(""))
	                {
	                    reading.yArray.add(Double.parseDouble(tempString));
	                }
				}
				for(int tempi = 0;tempi<listt.size();tempi++){
					String tempString = listt.get(tempi);
					if (tempString != null && !tempString.equals(""))
	                {
	                    reading.tArray.add(Long.parseLong(tempString));
	                }
				}
                break;
				
            }
            System.out.println("Start Advertising...\n");
            update();
           // sendSSLMessage(emailFromAddress, reading.recipients, emailSubjectTxt,
             //          emailMsgTxt);
            System.out.println("Data Advertised\n");
            break;

        case "Emergency":
			
		
            String sup = kvList.getValue("MainComponent");
            String auxs = kvList.getValue("HelperComponents");
            String note = kvList.getValue("Note");

            System.out.println("\n*** Emergency Alert from " + sup + " backed by " + auxs+" ***");

            System.out.println("Start Advertising...\n");
            update();
            sendAlertSSLMessage(emailFromAddress, reading.recipients,
                                "Emergency Alert from " + sup + " backed by " + auxs, note);
            System.out.println("Data Advertised\n");
            break;

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
                case "SwitchUser":
                    String user = kvList.getValue("UserID");
                    reading.uid = user;
                    System.out.println("User Login: "+user);
                    break;
                case "UpdateRecipients":
                    String recs = kvList.getValue("Recipients");
                    reading.recipients = recs.replaceAll("\\s+", "").split(",",0);
					System.out.println("Recipients Updated: "+Arrays.toString(reading.recipients)+" "+reading.recipients.length);
                    break;
                case "ForceUpload":
                    update();
                    break;

                case "Kill":
                    System.exit(0);
                    break;
                }
            }
            break;
        }
    }

    private static void execute(String query) throws Exception {
        String url = "http://ksiresearch.org/chronobot/PHP_Post.php";
        PostQuery.PostToPHP(url, query);
    }

    private static String formQuery(long datetime, String source, String type, Object value){
        return "Insert into records (uid, datetime, source, type, value) values ('"
                        + reading.uid
                        + "',"
                        + "FROM_UNIXTIME("+datetime/1000+")"
                        + ",'"
						+ source
						+ "','"
                        + type
                        + "','"
                        + value.toString()
                        + "')";
    }

    private static void update() throws Exception
    {

        System.out.println(reading);

        System.out.println("Updating DB...");
	    execute(formQuery(reading.t,"ReadingBehavior","GazeX",reading.x));
        execute(formQuery(reading.t,"ReadingBehavior","GazeY",reading.y));
		
		
		int numTemp = Math.min(reading.xArray.size(),(Math.min(reading.yArray.size(),reading.tArray.size())));
		System.out.println(numTemp+" data will be updated to DB.");
		for(int tempi=0;tempi<numTemp;tempi++){
        	execute(formQuery(reading.tArray.get(tempi),"ReadingBehavior","GazeXArray",reading.xArray.get(tempi)));
        	execute(formQuery(reading.tArray.get(tempi),"ReadingBehavior","GazeYArray",reading.yArray.get(tempi)));
        }
		

        
        System.out.println("DB Updated");
        // sendSSLMessage(sendTo, emailSubjectTxt, emailMsgTxt,
        // emailFromAddress, lname, fname, spo2);
    }
}

class UploaderReading
{
	double x;
	double y;
	long t;
	ArrayList<Double> xArray = new ArrayList<Double>();
	ArrayList<Double> yArray = new ArrayList<Double>();
	ArrayList<Long> tArray = new ArrayList<Long>();
	
    String uid = "376896";
    String firstName = "Shi-Kuo";
    String lastName = "Chang";
    String[] recipients = { "sisfortest@outlook.com",
                            "chronobot@ksiresearch.org"
                          };


    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        StringBuilder builder = new StringBuilder();
        builder.append("----------------------------------------------\n");
		builder.append("GazeX: " + x + "\n");
		builder.append("GazeY: " + y + "\n");
		builder.append("Timestamp: " + t + "\n");
		builder.append("xArray Size" + xArray.size()+"\n");
		builder.append("yArray Size" + yArray.size()+"\n");
		builder.append("tArray Size" + tArray.size()+"\n");
    /*    builder.append("First Name: " + firstName + "\n");
        builder.append("Last Name: " + lastName + "\n");
        builder.append("Email: " + Arrays.toString(recipients) + "\n\n");
        builder.append("Systolic: " + systolic + "\n");
        builder.append("Diastolic: " + diastolic + "\n");
        builder.append("Pulse: " + pulse + "\n");
        builder.append("Date (Blood Pressure): " + dateBP + "\n\n");
        builder.append("EKG: " + ekg + "\n");
        builder.append("Date (EKG): " + dateEKG + "\n\n");
        builder.append("SPO2: " + spo2 + "\n");
        builder.append("Date (SPO2): " + dateSPO2 + "\n\n");
        builder.append("Temperature: " + temp + "\n");
        builder.append("Date (Temperature): " + dateTemp + "\n\n");
        builder.append("Kinect Status: " + kinectStatus + "\n");
        builder.append("Date (Kinect): " + dateKinect + "\n");*/
        builder.append("----------------------------------------------\n");
        return builder.toString();
    }
}
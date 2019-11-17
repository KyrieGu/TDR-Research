package tdr.audio;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

import javax.mail.MessagingException;


/**
 * Created by mzhang on 12/1/16.
 */

class SocketsThread {
    static final String SMTP_HOST_NAME = "smtp.ksiresearch.org.ipage.com";
    static final String SMTP_PORT = "587";
    static final String emailFromAddress = "chronobot@ksiresearch.org";
    static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final int duration = Toast.LENGTH_SHORT;
    static final private String TAG = "SocketsThread";
    static final private String MyName = "AndroidUploader";
    private static UploaderReading reading = new UploaderReading();
    private static int counter = 1;
    private int connectionNumber;
    private MsgDecoder msgDecoder;
    private MsgEncoder msgEncoder;
    private Activity activity;


    private static boolean execute(String query) throws Exception {
//        String url = "http://ksiresearch.org/chronobot/PHP_Post_copy.php";
        String url = "http://ksiresearch.org/chronobot/PHP_Post.php";//Duncan add Feb 2017
        return PostQuery.PostToPHP(url, query);
    }

    private static String formQuery(long datetime, String source, String type, Object value) {
        return "Insert into `records` (`uid`, `datetime`, `source`, `type`, `value`) values ('"
                + reading.uid
                + "',"
                + "FROM_UNIXTIME(" + datetime / 1000 + ")"
                + ",'"
                + source
                + "','"
                + type
                + "','"
                + value.toString()
                + "')";
    }
    private static String formQuery(String datetime, String source, String type, Object value, String originator){
        String q = "Insert into records (uid, datetime, source, type, value) values ('"
                + reading.uid
                + "','"
                + datetime
                + "','"
                + source
                + "','"
                + type
                + "','"
                + value.toString()
                + "','"
                +originator
                + "')";
        System.out.println(q);
        return q;
    }

    //@Overload to add Originator
    private static String formQuery(long datetime, String source, String type, Object value, String originator){
        return "Insert into records (uid, datetime, source, type, value, originator) values ('"
                + reading.uid
                + "',"
                + "FROM_UNIXTIME("+datetime/1000+")"
                + ",'"
                + source
                + "','"
                + type
                + "','"
                + value.toString()
                + "','"
                +originator
                + "')";
    }

    static void processMessage(KeyValueList kvList) {
        final String scope = kvList.getValue("Scope");
        final String messageType = kvList.getValue("MessageType");
        final String sender = kvList.getValue("Sender");
        Log.d(TAG, "Message Received: " + kvList.toString());


        //String scope = kvList.getValue("Scope");
        System.out.println(scope);
    /*   if (!SCOPE.startsWith(scope))
        {
            return;
        }

       // String messageType = kvList.getValue("MessageType");
        if (!TYPES.contains(messageType))
        {
            return;
        }*/

        //String sender = kvList.getValue("Sender");
        System.out.println(sender);
        String receiver = kvList.getValue("Receiver");
        System.out.println(receiver);
        String purpose = kvList.getValue("Purpose");
        System.out.println(purpose);
        System.out.println(messageType);

        switch (messageType) {
            case "Reading":
/*                reading.message_sender = sender;
                reading.BP = kvList.getValue("Data_BP");
                reading.ECG = kvList.getValue("Data_ECG");
                reading.EMG = kvList.getValue("Data_EMG");
                reading.Pulse = kvList.getValue("Data_Pulse");

                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                reading.readable_time = date_format.format(new Date(reading.collection_time));
                Log.d(TAG, "Readable Date: " + reading.readable_time);

                String emailSubject = "Android App Readings from " + sender;
                String emailContent = kvList.toString();*/
                reading.up = kvList.getValue("Sender");
                reading.purpose = kvList.getValue("Purpose");
                reading.value = kvList.getValue("Value");
                reading.type = kvList.getValue("Type");
                System.out.println("++++++++++++++++++");
                System.out.println(purpose);

                System.out.println("++++++++++++++++++");
                String sUid = kvList.getValue("uid");
/*                String[] sChannels = new String[Integer.parseInt(kvList.getValue("channels"))];
                for (int i = 0; i < sChannels.length; i++) {
                    sChannels[i] = kvList.getValue("channels"+i);
                }
*/
                String sDate = kvList.getValue("datetime");
                String sOriginator = kvList.getValue("originator");

                if (sUid != null && !sUid.equals("")) {
                    // TODO for testing, I don't need to change the uid here.
                    reading.uid = sUid;
                }/*
                reading.channels = new double[Integer.parseInt(kvList.getValue("channels"))];
                for (int i = 0; i < reading.channels.length; i++) {
                    if (sChannels[i] != null && !sChannels[i].equals("")) {
                        reading.channels[i] = Double.parseDouble(sChannels[i]);
                    }
                }*/

                if (sDate != null && !sDate.equals("")) {
                    reading.dateBCISensor = Long.parseLong(sDate);
                }
                if (sOriginator != null && !sOriginator.equals("")){
                    reading.originator = sOriginator;
                }

                try {
                    send_msg();
                    //send_email(emailFromAddress, reading.recipients, emailSubject, emailContent);
                    //sendSSLMessage(emailFromAddress, reading.recipients, emailSubject, emailContent);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Register":

                break;
        }
    }

    static void send_msg() throws Exception {
/*        boolean result;
        result = execute(formQuery(reading.collection_time, "Android_BP", "BloodPressure", reading.BP));
        result = result && execute(formQuery(reading.collection_time, "Android_EMG", "Electromyography", reading.EMG));
        result = result && execute(formQuery(reading.collection_time, "Android_ECG", "Electrocardiogram", reading.ECG));
        result = result && execute(formQuery(reading.collection_time, "Android_Pulse", "Heart Rate", reading.Pulse));

        Log.d(TAG, "Update to database completed.");*/

//        System.out.println(reading.toString());

        System.out.println("Updating DB...");
//        switch(reading.up) {
            //case "BCIMonitor"://Duncan delete Jan 2017
  //          case "BCIFilter"://Duncan add Jan 2017
                //execute(formQuery(reading.dateVotes,"SocialNetwork","votes",reading.votes));
                if (reading.purpose.equals("upload")) {
                    System.out.println("Android BCIUploader");
 //                   for ( int i = 0; i < reading.channels.length; i++) {
                        execute(formQuery(reading.dateBCISensor,reading.up,reading.type,reading.value, reading.originator));
//                    }

                }

    //            break;
 //       }


        System.out.println("DB Updated");
    }

/*
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

                    part.setContent(message, "text/html");
                    newMultipart.addBodyPart(part);
                }
            }

            msg.setContent(multipart);

        }
        Transport.send(msg);
        System.out.println("Successfully Sent mail to All Users, lol.\n");
    }*/
}
/*
class UploaderReading {
    String uid = "376896";
    String firstName = "Shi-Kuo";
    String lastName = "Chang";
    String[] recipients = {"sisfortest@outlook.com",
            "chronobot@ksiresearch.org"
    };

    long collection_time;
    String readable_time;
    String BP;
    String EMG;
    String ECG;
    String Pulse;
    String message_sender;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("----------------------------------------------\n");
        builder.append("First Name: " + firstName + "\n");
        builder.append("Last Name: " + lastName + "\n");
        builder.append("Email: " + Arrays.toString(recipients) + "\n\n");
        builder.append("Device: " + message_sender + "\n");
        builder.append("Collection Time: " + readable_time + "\n\n");
        builder.append("Blood Pressure: " + BP + "\n");
        builder.append("Electromyography: " + ECG + "\n");
        builder.append("Electrocardiogram: " + EMG + "\n");
        builder.append("Heart Rate: " + Pulse + "\n");
        builder.append("----------------------------------------------\n");
        return builder.toString();
    }
}*/

class UploaderReading
{
    String uid = "376896";
    String firstName = "Shi-Kuo";
    String lastName = "Chang";
    String[] recipients = { "sisfortest@outlook.com",
            "chronobot@ksiresearch.org"
    };
    double[] channels;
    String up = "";
    String purpose = "";
    String originator = "";
    long dateBCISensor;
    String value="";
    String type="";
    //double score;

    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        StringBuilder builder = new StringBuilder();
        builder.append("----------------------------------------------\n");
        builder.append("First Name: " + firstName + "\n");
        builder.append("Last Name: " + lastName + "\n");
        builder.append("Email: " + Arrays.toString(recipients) + "\n\n");
        for (int i = 0; i < channels.length; i++){
            builder.append("Channel " + i + ": " + channels[i] + "\n");
        }

        builder.append("UserID: " + uid + "\n");
        builder.append("Date (BCI): " + dateBCISensor + "\n");
        builder.append("----------------------------------------------\n");
        return builder.toString();
    }
}
//    static void send_email(String from, String[] recipients,
//                           String subject, String content) throws MessagingException {
//        Log.d("Email", "Start to send Email.");
//        boolean debug = false;
//        Properties props = new Properties();
//        props.put("mail.smtp.host", SMTP_HOST_NAME);
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", SMTP_PORT);
//        props.put("mail.smtp.ssl.enable", "true");
//        Log.d("Email", "Set parameters successfully.");
//
//        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("chronobot@ksiresearch.org", "Health14");
//            }
//        });
//        Log.d("Email", "Sessions created successfully.");
//        session.setDebug(debug);
//
//        Message msg = new MimeMessage(session);
//        InternetAddress addressFrom = new InternetAddress(from);
//        msg.setFrom(addressFrom);
//
//        InternetAddress[] addressTo = new InternetAddress[recipients.length];
//        for (int i = 0; i < recipients.length; i++) {
//            addressTo[i] = new InternetAddress(recipients[i]);
//        }
//        msg.setRecipients(Message.RecipientType.TO, addressTo);
//
//        msg.setSubject(subject);
//
//        msg.setText(reading.toString());
//
//        Log.d("Email", "Ready to send out messages.");
//        Transport.send(msg);
//
//        Log.d("Email", "Send sensor readings to recipients' Email successfully!");
//    }

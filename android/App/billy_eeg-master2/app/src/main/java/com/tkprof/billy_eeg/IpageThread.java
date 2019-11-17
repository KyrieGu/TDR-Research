package com.tkprof.billy_eeg;

/**
 * Created by LilyKim on 2/22/2018.
 */

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

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



public class IpageThread implements Runnable {
    static final String SMTP_HOST_NAME = "smtp.ksiresearch.org.ipage.com";
    static final String SMTP_PORT = "587";
    static final String emailFromAddress = "chronobot@ksiresearch.org";
    static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final int duration = Toast.LENGTH_SHORT;
    static final private String TAG = "SocketsThread";
    static final private String MyName = "BrainWave";
    static String emailSubject = "Android App Readings from " + MyName;
    static String[] recipients = {"chang@pitt.edu",
            "chronobot@ksiresearch.org"
    };
    static String uid = "777777";
    static String source = "BrainWave";
    static String originator = "headset";
    static String value = "";
    static String type = "";
    static String raw_data = "";
    private String query;
    public static boolean control = false;
    IpageThread(String q, String v, String t, String uid, String raw_data) {
        this.query = q;
        this.value = v;
        this.type = t;
        this.uid = uid;
        this.raw_data = raw_data;
    }

    @Override
    public void run() {
        if (query == "insertBrainwave") {
            long curr_time = System.currentTimeMillis();
            try{
                long dt = Long.parseLong(String.valueOf(curr_time));
                //if(control == false) {
                    execute(insertQuery(dt, source, type, value, originator), dt);
                //}


            }catch (MessagingException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static boolean execute(String[] query, long datetime) throws Exception {
        String url = "http://ksiresearch.org/chronobot/PHP_Post.php";
        return PostToPHP(url, query, datetime);
    }

    //@Overload to add
    private static String[] insertQuery(long datetime, String source, String type, String value, String originator){
        String[] queries = new String[3];
        queries[0] = "Insert into records (uid, datetime, source, type, value, originator) values ('"
                + uid + "'," + "FROM_UNIXTIME("+datetime/1000+")" + ",'"
                + source + "','" + "Attention" + "','" + type + "','" +originator + "')";
        queries[1] = "Insert into records (uid, datetime, source, type, value, originator) values ('"
                + uid + "'," + "FROM_UNIXTIME("+datetime/1000+")" + ",'"
                + source + "','" + "Meditation" + "','" + value + "','" +originator + "')";
        queries[2] = "Insert into records (uid, datetime, source, type, value, originator) values ('"
                + uid + "'," + "FROM_UNIXTIME("+datetime/1000+")" + ",'"
                + source + "','" + "BrainWaveData" + "','" + raw_data + "','" +originator + "')";
        return queries;
    }

    public static boolean PostToPHP(String UrlIn, String[] QueryIn, long datetime) {
        HttpURLConnection conn = null;
        boolean response = false;
        for(String s: QueryIn) {
            if (s.length() > 0) {
                try {
                    URL url = new URL(UrlIn);
                    String agent = "Applet";
                    String query = "query=" + s;
                    String type = "application/x-www-form-urlencoded";

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", agent);
                    conn.setRequestProperty("Content-Type", type);
                    conn.setRequestProperty("Content-Length", "" + query.length());
                    OutputStream out = conn.getOutputStream();
                    out.write(query.getBytes());
                    out.flush();
                    conn.connect();

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    String reply = "";
                    while ((inputLine = in.readLine()) != null) {
                        Log.d("PostQuery", inputLine);
                        reply += inputLine;
                    }
                    if (reply.equals("success")) {
                        response = true;
                    }
                    in.close();
                } catch (Exception e) {
                    Log.d("PostQuery", e.toString());
                } finally {
                    conn.disconnect();
                    //Thread.sleep(500);

                }
            }
        }
        try {

            String emailContent = "";
            emailContent = toString(datetime, source, "Attention", type, originator) + "\n";
            emailContent = emailContent + toString(datetime, source, "Mediation", value, originator) + "\n";
            emailContent = emailContent + toString(datetime, source, "BrainWaveData", raw_data, originator);
            sendSSLMessage(emailFromAddress, recipients, emailSubject, emailContent);
            //setControlTrue();
        }
        catch(MessagingException e){
            e.printStackTrace();
        }

        return response;
    }
    static synchronized void setControlTrue(){
        control = true;
    }
    static synchronized void setControlFalse(){
        control = false;
    }
    static String toString(long datetime, String source, String type, String value, String originator) throws MessagingException {

        String str = "";
        str = str + "uid: " + uid;
        str = str + "|datetime: " + datetime;
        str = str + "|source: " + source;
        str = str + "|type: "  + type;
        str = str + "|value: " + value;
        str = str + "|orginator: " + originator;
        return str;
    }
    public static void sendSSLMessage(String from, String recipients[],
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
    }
}


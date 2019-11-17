//package UpLoader;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.applet.*;
import java.security.*;

public class PostQuery {
    public static void PostToPHP(String UrlIn, String QueryIn){
        HttpURLConnection conn=null;
        try{
        URL url=new URL(UrlIn);
        String agent="Applet";
        String query="query=" + QueryIn;
        String type="application/x-www-form-urlencoded";
        conn=(HttpURLConnection)url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty( "User-Agent", agent );
        conn.setRequestProperty( "Content-Type", type );
        conn.setRequestProperty( "Content-Length", ""+query.length());
        OutputStream out=conn.getOutputStream();
        out.write(query.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while((inputLine=in.readLine())!=null){
            //System.out.print(inputLine+"\n");
        }
        in.close();
        int grc = conn.getResponseCode();
        //System.out.print("ResponseCode = "+ grc +"\n");
        String grm=conn.getResponseMessage();
        //System.out.print("ResponseMessage = "+ grm +"\n");
        }catch(Exception e){
        e.printStackTrace();
        }finally{
        conn.disconnect();
        }
    }

    public static void main(String[] args){
        String url = "http://ksiresearch.org/chronobot/PHP_Post.php";
        PostQuery p = new PostQuery();
        String query;
        p.PostToPHP(url, formquery("376905", "WebGUI", "tongue","1"));
        p.PostToPHP(url, formquery("376905", "WebGUI", "fatigue","4"));
        p.PostToPHP(url, formquery("376905", "WebGUI", "weakBreadth","2"));
        p.PostToPHP(url, formquery("376905", "WebGUI", "pulse","10"));
        p.PostToPHP(url, formquery("376905", "WebGUI", "sweaty","3"));
        query = "Insert into records (uid, datetime, source, type, value) values ('"
                        + "376905"
                        + "',"
                        + "FROM_UNIXTIME("+System.currentTimeMillis()/1000+")"
                        + ",'"
                        + "WebGUI"
                        + "','"
                        + "Similar"
                        + "','"
                        + "request"
                        + "')";
        p.PostToPHP(url,query);

    }

    public static String formquery(String uid, String source, String type, String value){
        return "Insert into records (uid, datetime, source, type, value) values ('"
                        + uid
                        + "',"
                        + "FROM_UNIXTIME("+System.currentTimeMillis()/1000+")"
                        + ",'"
                        + source
                        + "','"
                        + type
                        + "','"
                        + value
                        + "')";
    }
}
import java.io.*;
import java.util.*;
import java.net.*;

public class KinectSensorReader{
	private Thread thread;
    private ReadTask readTask;

    public KinectSensorReader(MsgEncoder en) throws Exception
    {
        readTask = new ReadTask(en);
        thread = new Thread(readTask);
        thread.start();
    }

    public void close()
    {

        try
        {
            readTask.terminate();
            thread.join();
            // universal.shutdownInput();
            // universal.shutdownOutput();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

class ReadTask implements Runnable
{

    private volatile boolean running = true;

    MsgEncoder encoder;
	ServerSocket serverSocket;

    public ReadTask(MsgEncoder en) throws Exception
    {
        // TODO Auto-generated constructor stub
        encoder = en;
        init();
    }

    private void init() throws Exception
    {
        if (isProcessRunning("KinectExplorer-WPF.exe")) {
            killProcess("KinectExplorer-WPF.exe");
        }
        Runtime.getRuntime().exec("KinectExplorer-WPF.exe");
        serverSocket = new ServerSocket(9999);
    }

    public static boolean isProcessRunning(String serviceName) throws Exception {
        Process p = Runtime.getRuntime().exec("tasklist");
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public static void killProcess(String serviceName) throws Exception {
        Runtime.getRuntime().exec("taskkill /IM " + serviceName);
    }

    public void run()
    {
        Socket socket;
        try{
        	socket = serverSocket.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//System.out.println("Read!");
			String line = reader.readLine();
			//System.out.println("Read one!");
			while (running) {
				if (line != null && !line.equals("")) {
					//System.out.println(line);
					KeyValueList list = KeyValueList.decodedKV(line);
					if(list!=null){
						String val = list.getValue("MsgID");
						if(val!=null&&!val.equals("")){
							int MsgID = Integer.parseInt(val);
							String sta = list.getValue("Status");
							if(MsgID==43&&!sta.equals("")&&!sta.equals("Normal")){
                                list.putPair("Name","KinectSensor");
                                list.putPair("Scope", CreateKinectSensor.SCOPE);
                                list.putPair("MessageType", "Setting");
                                list.putPair("Sender", CreateKinectSensor.NAME);
                                list.putPair("Receiver", "KinectMonitor");
                                list.putPair("Purpose", "UpdateRecord");
                                encoder.sendMsg(list);
                                System.out.println(list);
							}
						}
					}
				}
				//System.out.println("Read!");
				line = reader.readLine();
				//System.out.println("Read one!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    }

    public void terminate()
    {
        try
        {
            running = false;
            serverSocket.close();
            if (isProcessRunning("KinectExplorer-WPF.exe")) {
                killProcess("KinectExplorer-WPF.exe");
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

//lass KinectReading {
//	KeyValueList msg43;
//}
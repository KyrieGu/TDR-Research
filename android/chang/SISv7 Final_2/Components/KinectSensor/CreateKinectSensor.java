import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CreateKinectSensor {
	// socket for connection to SISServer
	static Socket universal;
	private static int port = 53217;
	// message writer
	static MsgEncoder encoder;
	// message reader
	static MsgDecoder decoder;

	// scope of this component
	public static final String SCOPE = "SIS.Ren";
	// name of this component
    public static final String NAME = "KinectSensor";
	// messages types that can be handled by this component
	private static final List<String> TYPES = new ArrayList<String>(
			Arrays.asList(new String[] { "Setting", "Confirm" }));

	//private static Timer timer = new Timer();

	//private static ServerSocket sensorServerSocket;
	//private static Socket sensorSocket;
	//private static BufferedReader sensorReader;
	private static KinectSensorReader reader;
	/*
	 * Main program
	 */
	public static void main(String[] args) {
		while (true) {
			try {
				// try to establish a connection to SISServer
				universal = connect();

				// bind the message reader to inputstream of the socket
				decoder = new MsgDecoder(universal.getInputStream());
				// bind the message writer to outputstream of the socket
				encoder = new MsgEncoder(universal.getOutputStream());

				/*
				 * construct a message 23 to establish the connection
				 */
				KeyValueList conn = new KeyValueList();
				conn.putPair("Scope", SCOPE);
				conn.putPair("MessageType", "Connect");
				conn.putPair("Role", "_Basic");
				conn.putPair("Name", NAME);
				encoder.sendMsg(conn);

				// KeyValueList for inward messages, see KeyValueList for
				// details

				//sensorServerSocket = new ServerSocket(9999);
				//Runtime.getRuntime().exec("KinectExplorer-WPF.exe");
				//sensorSocket = sensorServerSocket.accept();
				//sensorReader = new BufferedReader(new InputStreamReader(
				//		sensorSocket.getInputStream()));

				reader = new KinectSensorReader(encoder);
				KeyValueList kvList = null;
				while (true) {
					try {

						kvList = decoder.getMsg();
						if (kvList != null) {
							ProcessMsg(kvList);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				// if anything goes wrong, try to re-establish the connection
				try {
					// wait for 1 second to retry
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
				}
				System.out.println("Try to reconnect");
				try {
					universal = connect();
				} catch (IOException e1) {
				}
			}

		}
	}

	/*
	 * used for connect(reconnect) to SISServer
	 */
	static Socket connect() throws IOException {
		Socket socket = new Socket("127.0.0.1", port);
		return socket;
	}

	private static void componentTask() {
		try {

			/*String line = sensorReader.readLine();
			// while (true) {
			if (line != null && !line.equals("")) {
				System.out.println(line);
				KeyValueList list = KeyValueList.decodedKV(line);
				if (list != null) {
					String val = list.getValue("MsgID");
					if (val != null) {
						int MsgID = Integer.parseInt(list.getValue("MsgID"));

						if (MsgID == 43) {
							list.putPair("Scope", SCOPE);
							list.putPair("MessageType", "Setting");
							list.putPair("Sender", NAME);
							list.putPair("Receiver", "KinectMonitor");
							list.putPair("Purpose", "UpdateRecord");
							encoder.sendMsg(list);
						}
					}
				}
			}
			// line = reader.readLine();
			// }
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void ProcessMsg(KeyValueList kvList) throws Exception {

		String scope = kvList.getValue("Scope");
		if (!SCOPE.startsWith(scope)) {
			return;
		}

		String messageType = kvList.getValue("MessageType");
		if (!TYPES.contains(messageType)) {
			return;
		}

		String sender = kvList.getValue("Sender");

		String receiver = kvList.getValue("Receiver");

		String purpose = kvList.getValue("Purpose");

		switch (messageType) {
		case "Confirm":
			System.out.println("Connect to SISServer successful.");
			break;
		case "Setting":
			if (receiver.equals(NAME)) {
				System.out.println("Message from " + sender);
				System.out.println("Message type: " + messageType);
				System.out.println("Message Purpose: " + purpose);
				switch (purpose) {

				case "Kill":
					reader.close();
					System.exit(0);
					break;
				case "Activate":
					/*try {
						timer.cancel();
						timer = new Timer();
					} catch (Exception e) {
						// TODO: handle exception
					}
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							componentTask();
						}
					}, 0, 0);*/
					reader = new KinectSensorReader(encoder);
					System.out.println("Algorithm Activated");
					break;
				case "Deactivate":
					/*try {
						timer.cancel();
					} catch (Exception e) {
						// TODO: handle exception
					}*/
					reader.close();
					System.out.println("Algorithm Deactivated");
					break;
				}
			}
			break;
		}
	}

}

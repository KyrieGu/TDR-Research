import java.util.*;
import java.io.*;

enum State {
	NORMAL,
	ALERT;
}

class StateMachine {
	private String NAME;//controller name
	private String SCOPE;//controller scope
	private State state = State.NORMAL;//current state
	private KeyValueList setMsg = new KeyValueList();//setting message
	List<String> receivers = new ArrayList<String>(Arrays.asList(new String[] { "BloodPressure" }));//components in current scope
	private MsgEncoder encoder;//util for sending message, contains socket
	private Set<String> alertSet = new HashSet<String>();
	
	public StateMachine(String name, String scope, MsgEncoder encoder) {
		this.NAME = name;
		this.SCOPE = scope;
		this.encoder = encoder;
		initialMsg();
	}

	public void changeState(String inputMsg, String sender) throws IOException{
		switch (state) {
			case NORMAL:
				switch (inputMsg) {
					case "Alert":
						this.state = State.ALERT;
						alertSet.add(sender);
						System.out.println("Input message: " + inputMsg);
						System.out.println("State changed from NORMAL to ALERT");
						break;
					case "Activate":
						activeAllComponents();
						break;
					case "Deactivate":
						deactivateAllComponents();
						break;
					case "Kill":
						killAllComponents();
						System.exit(0);
					default:
						System.out.println("Input message: " + inputMsg);
						System.out.println("Stay in current state " + this.state);
				}
				break;

			case ALERT:
				switch (inputMsg) {
					case "Reading":
						System.out.println("Input message: " + inputMsg);
						alertSet.remove(sender);
						if (alertSet.isEmpty()) {
							this.state = State.NORMAL;
							System.out.println("State changed from ALERT to NORMAL");
						}
						System.out.println("Stay in current state Alert");
						break;
					case "Activate":
						activeAllComponents();
						break;
					case "Deactivate":
						System.out.println("Under Alert state, cannot deactivate components.");
						break;
					default:
						System.out.println("Input message: " + inputMsg);
						System.out.println("Stay in current state " + this.state);
				}
				break;
		}
	}

	private void initialMsg() {
		setMsg.putPair("Scope", SCOPE);
		setMsg.putPair("Sender", NAME);
		setMsg.putPair("MessageType", "Setting");
	}

	private void activeAllComponents() throws IOException{
		setMsg.putPair("Purpose", "Activate");
		sendMsgToAllComponets();
		setMsg.removePair("Purpose");
	}

	private void deactivateAllComponents() throws IOException{
		setMsg.putPair("Purpose", "Deactivate");
		sendMsgToAllComponets();
		setMsg.removePair("Purpose");
	}

	private void killAllComponents() throws IOException {
		setMsg.putPair("Purpose", "Kill");
		sendMsgToAllComponets();
		setMsg.removePair("Purpose");
	}

	private void sendMsgToAllComponets() throws IOException {
		for (String receiver : receivers) {
			setMsg.putPair("Receiver", receiver);
			encoder.sendMsg(setMsg);
			setMsg.removePair("Receiver");
		}
	}
}
import java.util.*;
import java.io.*;

enum State {
	// extends as many states as you need here
	NORMAL,
	ALERT;
}

class StateMachine {
	//controller name
	private String NAME;
	//controller scope
	private String SCOPE;
	//current state
	private State state = State.NORMAL;
	//setting message
	private KeyValueList setMsg = new KeyValueList();
	//components in current scope
	List<String> receivers = new ArrayList<String>(Arrays.asList(new String[] { !senders! }));
	//util for sending message, contains socket
	private MsgEncoder encoder;
	
	public StateMachine(String name, String scope, MsgEncoder encoder) {
		this.NAME = name;
		this.SCOPE = scope;
		this.encoder = encoder;
		initialMsg();
	}

	public void changeState(String inputMsg) throws IOException{
		switch (state) {
			case NORMAL:
				switch (inputMsg) {
					case "Alert":
						this.state = State.ALERT;
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
						this.state = State.NORMAL;
						System.out.println("Input message: " + inputMsg);
						System.out.println("State changed from ALERT to NORMAL");
						break;
					case "Activate":
						activeAllComponents();
						break;
					case "Deactivate":
						System.out.println("Under Alert state, cannot deactivate components.");
						break;
					case "Kill":
						System.out.println("Under Alert state, cannot kill components.");
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
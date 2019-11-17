import java.util.*;
import java.io.*;

enum BCIState {
    NORMAL,
    ALERT;
}

class BCIStateMachine {
    private String NAME;//controller name
    private String SCOPE;//controller scope
    private BCIState state = BCIState.NORMAL;//current state
    private KeyValueList setMsg = new KeyValueList();//setting message
    List<String> receivers = new ArrayList<String>(Arrays.asList(new String[]{"Temp"}));//components in current scope
    private MsgEncoder encoder;//util for sending message, contains socket

    public BCIStateMachine(String name, String scope, MsgEncoder encoder) {
        this.NAME = name;
        this.SCOPE = scope;
        this.encoder = encoder;
        initialMsg();
    }

    public void changeState(String messageType, String purpose) throws IOException {
        switch (state) {
            case NORMAL:
                switch (messageType) {
                    case "Alert":
                        this.state = BCIState.ALERT;
                        System.out.println("Input message: " + messageType);
                        System.out.println("State changed from NORMAL to ALERT");
                        break;
                    case "Emergency":
                        break;
                    case "Setting":
                        switch (purpose) {
                            case "Activate":
                                activeAllComponents();
                                break;
                            case "Deactivate":
                                deactivateAllComponents();
                                break;
                            case "Kill":
                                killAllComponents();
                                System.exit(0);
                                break;
                        }
                        break;
                    default:
                        System.out.println("Input message: " + messageType);
                        System.out.println("Stay in current state " + this.state);
                }
                break;

            case ALERT:
                switch (messageType) {
                    case "Reading":
                        this.state = BCIState.NORMAL;
                        System.out.println("Input message: " + messageType);
                        System.out.println("State changed from ALERT to NORMAL");
                        break;
                    case "Emergency":
                        break;
                    case "Setting":
                        switch (purpose) {
                            case "Activate":
                                activeAllComponents();
                                break;
                            case "Deactivate":
                                System.out.println("Under Alert state, cannot deactivate components.");
                                break;
                            case "Kill":
                                System.out.println("Under Alert state, cannot kill components.");
                                break;
                        }
                        break;
                    default:
                        System.out.println("Input message: " + messageType);
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

    private void activeAllComponents() throws IOException {
        setMsg.putPair("Purpose", "Activate");
        sendMsgToAllComponets();
        setMsg.removePair("Purpose");
    }

    private void deactivateAllComponents() throws IOException {
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
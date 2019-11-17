import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/* Serial data state */
enum Data_State
{
    ST_IDLE, ST_PT_FLAG, ST_PT_DATA, ST_ECG_STATUS, ST_ECG_DATA
}

public class NewSerialReader
{

    InputProcessorReading reading = new InputProcessorReading();

    private Thread thread;
    private ReadTask readTask;

    public NewSerialReader()
    {
        readTask = new ReadTask(reading);
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
    private SerialPort serialPort;
    private InputStream inputStream;

    InputProcessorReading reading = new InputProcessorReading();

    Data_State state = Data_State.ST_IDLE;
    int PT_BUFSIZE = 92; /* PT sequence size */
    StringBuffer pt_str = new StringBuffer(); /* PT data buffer */

    List<Byte> ecg_buf = new ArrayList<Byte>(); /* ECG data buffer */
    int ecg_seq = 0;

    // ECGData ecgData = new ECGData();

    boolean sync;

    // boolean alive = false;

    public ReadTask(InputProcessorReading re)
    {
        // TODO Auto-generated constructor stub
        reading = re;
        init();
    }

    private void init()
    {
        int port = 0;
        CommPortIdentifier portIdentifier;
        do
        {
            try
            {
                portIdentifier = CommPortIdentifier.getPortIdentifier("COM"
                                 + port);
                System.out.println("Listening on COM" + port + "......");
                break;
            }
            catch (NoSuchPortException e)
            {
                // TODO: handle exception
                port++;
            }
        }
        while (true);

        CommPort commPort = null;
        try
        {
            commPort = portIdentifier.open(this.getClass().getName(), 2000);
        }
        catch (PortInUseException e)
        {
            // TODO: handle exception
            System.out.println("Port " + port + " is currently used by "
                               + portIdentifier.getCurrentOwner());
        }

        if (commPort != null && commPort instanceof SerialPort)
        {
            serialPort = (SerialPort) commPort;
            // Set serial parameters
            try
            {
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                                               SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                inputStream = serialPort.getInputStream();

                // reader = new SerialReader(inputStream);

            }
            catch (UnsupportedCommOperationException e)
            {
                System.out.println("Driver doesn't allow this operation.");
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                System.out.println("Can't get input stream from port.");
            }
        }
    }

    public void run()
    {
        // alive = true;
        System.out.println("Start collecting info...");
        byte[] buf = new byte[256];
        int n = -1;
        try
        {
            while (running && (n = this.inputStream.read(buf)) > -1)
            {
                // if (n != 0)
                    // System.out.println("n: " + Arrays.toString(buf));

                for (int i = 0; i < n; i++)
                {
                	// System.out.println(state);
                    switch (state)
                    {
                    case ST_IDLE:
                        switch (buf[i])
                        {
                        case -128: // 0x80, Waveform message starts
                            ecg_buf.add(buf[i]);
                            state = Data_State.ST_ECG_STATUS;

                            break;

                        case 'P':// 80
                            pt_str.append((char) buf[i]);
                            state = Data_State.ST_PT_FLAG;
                            break;

                        case 'T':// 84
                            sync = false;
                            pt_str.append((char) buf[i]);
                            state = Data_State.ST_PT_DATA;
                            break;
                        default:
                            sync = false;
                            break;
                        } // switch(buf[i])

                        break; // case ST_IDLE

                    case ST_ECG_STATUS: /* ECG status byte */
                        //System.out.println(buf[i]);
                        if ((buf[i] & 0xf0) > 0) {
                            state = Data_State.ST_IDLE;
                            //break;[duncan] delete
                        }

                        if (sync) {
                            if ((buf[i] & InputProcessorReading.SEQ_MASK) != ecg_seq) {
                                sync = false;
                                state = Data_State.ST_IDLE;
                                break;
                            }
                        } else {
                            if ((buf[i] & InputProcessorReading.SEQ_MASK) == ecg_seq) {
                                sync = true;
                            }
                        }

                        ecg_seq = (buf[i] & InputProcessorReading.SEQ_MASK);
                        ecg_seq += 1;
                        ecg_seq &= InputProcessorReading.SEQ_MASK;
                        ecg_buf.add(buf[i]);
                        state = Data_State.ST_ECG_DATA;
                        break;

                    case ST_ECG_DATA:
                        // byte b = (byte) (buf[i] & 0xFF);
                        // ecg_buf.add(b);
                        ecg_buf.add(buf[i]);
                        // System.out.println(buf[i] & 0xFF);
                        if (ecg_buf.size() >= InputProcessorReading.ECG_MSGSIZE) {
                            if (sync) {
                                // // A ECG message is ready
                                // ECGMessage ecgMsg = new ECGMessage(ecg_buf);

                                //  //System.out.println("ecgMsg "+ecgMsg.getLeadII());
                                // // if (ecgMsg.isValid()) {
                                // if (ecgData.getNumMessages() < 10)
                                //     ecgData.addECGMessage(ecgMsg);
                                // // }
                                reading.process(ecg_buf);
                            }

                            // if (ecgData.getNumMessages() >= 1000)
                            // sendECGData();

                            ecg_buf.clear();
                            state = Data_State.ST_IDLE;
                        }

                        break;

                    case ST_PT_FLAG:
                        if (buf[i] == 'T')
                        {
                            pt_str.append((char) buf[i]);
                            state = Data_State.ST_PT_DATA;
                        }
                        else
                        {
                            sync = false;
                            if (buf[i] == 128)
                            {
                                state = Data_State.ST_ECG_STATUS;
                                break;
                            }
                            state = Data_State.ST_IDLE;
                            break;
                        }

                        break;
                    case ST_PT_DATA:
                        if (buf[i] == 128)
                        {
                            sync = false;
                            state = Data_State.ST_ECG_STATUS;
                            break;
                        }

                        if (!((buf[i] == 32) || ((buf[i] >= 48) && (buf[i] <= 57))))
                        {
                            sync = false;
                            state = Data_State.ST_IDLE;
                            break;
                        }

                        pt_str.append((char) buf[i]);
                        if (pt_str.length() >= PT_BUFSIZE)
                        {
                            sync = true;

                            // A complete PT string is ready
                            //System.out.println(pt_str.toString());

                            reading.process(pt_str.toString());

                            // reading.systolic = newReading.getSystolic();
                            // reading.diastolic = newReading.getDiastolic();
                            // reading.pulse = newReading.getPulse();
                            // reading.spo2 = newReading.getSPO2();
                            // reading.ekg = newReading.getEKG();
                            // reading.meanPressure = newReading.getMeanPressure();

                            // Send to the SIS server
                            // sendVitalData();

                            pt_str.delete(0, pt_str.length());

                            state = Data_State.ST_IDLE;
                        }

                        break;

                    default:
                        sync = false;
                        state = Data_State.ST_IDLE;
                        break;
                    }
                } // for
            } // while
        } // try
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        catch (Exception e2)
        {
            e2.printStackTrace();
        }
    }

    public void terminate()
    {
        try
        {
            running = false;
            inputStream.close();
            serialPort.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // public void stop() {
    // 	try {
    // 		inputStream.close();
    // 	} catch (IOException e) {
    // 		// TODO Auto-generated catch block
    // 		e.printStackTrace();
    // 	}
    // 	// alive = false;
    // }

    // public void start() {

    // 	try {
    // 		inputStream.close();
    // 		inputStream = serialPort.getInputStream();
    // 		run();
    // 	} catch (IOException e) {
    // 		// TODO Auto-generated catch block
    // 		e.printStackTrace();
    // 	}
    // 	// alive = false;
    // }
}

class InputProcessorReading
{

    // ECG/EKG //
    static final int ECG_FLAG = 0x80;
    static final byte SEQ_MASK = 0x03;
    static final int ECG_MSGSIZE = 8;
    
    private static final byte LEAD_MASK = 0x04;
    private static final byte LEADNUM_MASK = 0x08;

    private static final int SEQ_SIZE = 10; 
    private static int current_size = 0;

    private Queue<Integer> leadISeq = new LinkedBlockingQueue<>();  /* Lead I  */
    private Queue<Integer> leadIISeq = new LinkedBlockingQueue<>(); /* lead II */
    private Queue<Integer> leadIIISeq = new LinkedBlockingQueue<>();  /* lead III  */

    byte[] bArray = new byte[4];
    ByteBuffer bf;

    // ECG/EKG //

    private int systolic;
    private int diastolic;
    private int meanPressure;
    private int pulse;
    private int spo2;
    private int ekg;

    public InputProcessorReading()
    {

    }

    public void process(String ptStr)
    {
        // parse pt string, "PT****"
        String syst = ptStr.substring(2, 5).trim();
        if (syst.length() == 0)
            this.systolic = 0;
        else
        {
            int val = Integer.valueOf(syst);
            if (val > 0)
            {
                this.systolic = val;
            }
        }

        String dias = ptStr.substring(6, 9).trim();
        if (dias.length() == 0)
            this.diastolic = 0;
        else
        {
            int val = Integer.valueOf(dias);
            if (val > 0)
            {
                this.diastolic = val;
            }
        }
        String mean = ptStr.substring(10, 13).trim();
        if (mean.length() == 0)
            this.meanPressure = 0;
        else
            this.meanPressure = Integer.valueOf(mean);

        String pulse = ptStr.substring(14, 17).trim();
        if (pulse.length() == 0)
            this.pulse = 0;
        else
            this.pulse = Integer.valueOf(pulse);

        String spo2 = ptStr.substring(18, 21).trim();
        if (spo2.length() == 0)
            this.spo2 = 0;
        else
        {
            int val = Integer.valueOf(spo2);
            if (val > 0)
            {
                this.spo2 = val;
            }
        }
    }

    public void process(List<Byte> ecg_buf)
    {
    	// System.out.println("1" + ecg_buf);
        int leadI;
        int leadII;
        int leadIII;

        // Parse status byte
        // byte status = ecg_buf.get(1);
        // boolean leadsOn = ((status & LEAD_MASK) == 0);

        // if (!leadsOn) {
            // return;
        // }
        
        // int numLeads = -1;
        // if ((status & LEADNUM_MASK) == 0)
        //     numLeads = 3;
        // else
        //     numLeads = 5;
        
        // Parse lead data
        leadI  = ((ecg_buf.get(2) << 8) | ecg_buf.get(3)) - 32768;
        // leadII  = ((ecg_buf.get(4) << 8) | ecg_buf.get(5)) - 32768;
        
        bArray[0]=(byte)0;
        bArray[1]=(byte)0;
        bArray[2]=ecg_buf.get(4);
        bArray[3]=ecg_buf.get(5);

        bf = ByteBuffer.wrap(bArray);
        bf.order(ByteOrder.BIG_ENDIAN);
        //System.out.println(bf.getInt());
        
        leadII  = bf.getInt() - 32768;
        leadIII  = ((ecg_buf.get(6) << 8) | ecg_buf.get(7)) - 32768;

        if (current_size >= SEQ_SIZE) {
            leadISeq.poll();
            leadIISeq.poll();
            leadIIISeq.poll(); 
            current_size--;  
        }

        leadISeq.offer(leadI);
        leadIISeq.offer(leadII);
        leadIIISeq.offer(leadIII);
        current_size++;
    }

    public int getSystolic()
    {
        return this.systolic;
    }

    public int getDiastolic()
    {
        return this.diastolic;
    }

    public int getMeanPressure()
    {
        return this.meanPressure;
    }

    public int getPulse()
    {
        return this.pulse;
    }

    public int getSPO2()
    {
        return this.spo2;
    }

    public String getLeadISeq(){
        return leadISeq.toString(); //leadISeq.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    public String getLeadIISeq(){
        return leadIISeq.toString(); //leadIISeq.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    public String getLeadIIISeq(){
        return leadIIISeq.toString(); //leadIIISeq.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    @Override
    public String toString()
    {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("Systolic: ").append(this.systolic).append("; ");
        sbuf.append("Diastolic: ").append(this.diastolic).append("; ");
        sbuf.append("Mean: ").append(this.meanPressure).append("; ");
        sbuf.append("Pulse: ").append(this.pulse).append("; ");
        sbuf.append("SpO2: ").append(this.spo2);
        sbuf.append("LeadISeq: ").append(this.getLeadISeq());
        sbuf.append("LeadIISeq: ").append(this.getLeadIIISeq());
        sbuf.append("LeadIIISeq: ").append(this.getLeadIIISeq());

        return sbuf.toString();
    }
}
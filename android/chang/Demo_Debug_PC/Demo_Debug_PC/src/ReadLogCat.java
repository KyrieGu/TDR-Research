import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import org.jfree.ui.RefineryUtilities;

public class ReadLogCat {
	private static RTXYChart myChart;
	
	static int count = 0;
	static double [] samples = new double[50000];
	
	public static void process(String log){
//		System.out.println("-----log------"+log);
		if(log == null) return;	
		if (log.contains("EyeGazeData")) {
//			System.out.println("?HERE");
			//String prompt = "Gaze Detected!";
			//myChart.device_label.setText(html1 + "250" + html2 + prompt );
			//myChart.device_label.setForeground(Color.black);
			
			StringTokenizer st = new StringTokenizer(log, ":");
			//System.out.println(st.nextToken());
			st.nextToken();
			st.nextToken();
			st.nextToken();
			
			//myChart.minimum = -2;
			//myChart.maximum = 2;
			//System.out.println(st.nextToken());
			//System.out.println(st.nextToken());
			if(st.hasMoreTokens()){
				String s = st.nextToken();
				s = s.trim();
				StringTokenizer st2 = new StringTokenizer(s, ",");
				//st2.nextToken();
				
				long ts = Long.parseLong(st2.nextToken());
				long timestamp = ts;
				
				double valuex = Double.parseDouble(st2.nextToken());
				double valuey = Double.parseDouble(st2.nextToken());
				
				if(valuex==-100 && valuey==-100){
					myChart.series3.add(timestamp, 0);
					myChart.series4.add(timestamp, 0);
				}
				else{
					myChart.series1.add(timestamp, valuex);
					myChart.series2.add(timestamp, valuey);
				}
				
				
				
				if (count >= 50000) {
					//prompt = "Array Overflow! Please restart the application";
					//String html1 = "<html><body style='width: ";
			        //String html2 = "px'>";
					
			        //myChart.device_label.setForeground(Color.red);
					//myChart.device_label.setText(html1 + "250" + html2 + prompt );
					
					return;
				}	
								
			}
		}
		
		
		
	}
	
	public static void main(String args[]){
		myChart = new RTXYChart("Realtime Signal");
    	myChart.pack();
        RefineryUtilities.centerFrameOnScreen(myChart);
        myChart.setVisible(true);
        myChart.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        } );
        
		//System.out.println("logcat called\n");
		
		Process process = null;
//		Process process2 = null;
	    
	    try{
	    	process = Runtime.getRuntime().exec("adb logcat -c");
//	    	process2 = Runtime.getRuntime().exec("adb logcat -c");
	    	//System.out.println("Logcat cleaned");
	    	
	    	process = null;
//	    	process2 = null;

	    	ArrayList<String> commandLine = new ArrayList<String>();
			commandLine.clear();   
	    	commandLine.add("adb");
	    	commandLine.add("logcat");
	    	commandLine.add("EyeGazeData:I");
	    	commandLine.add("*:S");
	    	
	    	
//	    	ArrayList<String> commandLine2 = new ArrayList<String>();
//			commandLine2.clear();   
//	    	commandLine2.add("adb");
//	    	commandLine2.add("logcat");
//	    	commandLine2.add("EyeGazeData:I");
//	    	commandLine2.add("*:S");
	    	//process = Runtime.getRuntime().exec("adb logcat");
	    	process = Runtime.getRuntime().exec(commandLine.toArray(new String[commandLine.size()])); 
//	    	process2 = Runtime.getRuntime().exec(commandLine2.toArray(new String[commandLine2.size()])); 
	    	
	    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024); 
//	    	BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()), 1024); 
	    	//System.out.println("Logcat read");
	    	
	    	String line = "";
	    	String line2 = "";
	    	while ( (line = bufferedReader.readLine())!= null){
//	    		if(line!= null)
//	    		System.out.println(line);
	    			process(line);
//	    		else{
//	    			process(line2);
//	    		}
	    	}
	    } catch(Exception ex){
	    	//System.out.println(ex.toString());
	    	ex.printStackTrace();
	    	//System.out.println("error in Collector_logcat\n");
	    }
	    
	    return ;
	}
}

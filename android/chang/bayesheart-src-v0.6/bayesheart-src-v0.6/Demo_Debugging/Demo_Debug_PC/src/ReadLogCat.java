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
	final static String html1 = "<html><body style='width: ";
    final static String html2 = "px'>";
	
	static int count = 0;
	static double [] samples = new double[50000];
	
	public static void process(String log){
		if(log == null) return;
		
		if (log.startsWith("I/HeartBeatAlgorithm")) {
		
			String prompt = "LivePulse app detected!";
			myChart.device_label.setText(html1 + "250" + html2 + prompt );
			myChart.device_label.setForeground(Color.black);
			
			if(log.length() > 10){
				StringTokenizer st = new StringTokenizer(log, ":");
				st.nextToken();
				if(st.hasMoreTokens()){
					String s = st.nextToken();
					s = s.trim();
					
					if(s.startsWith("addSample")) {
						
						StringTokenizer st2 = new StringTokenizer(s, ",");
						st2.nextToken();
						
						long timestamp = Long.parseLong(st2.nextToken());
						double value = Double.parseDouble(st2.nextToken());
						
						if( value > -2000 && value < 2000 ){
							myChart.series1.add(timestamp, value);
						} else {
							if ( value < -2000 ) {
								myChart.series1.add(timestamp, -2000);
							} else {
								myChart.series1.add(timestamp, 2000);
							}
						}
						
						if (count >= 50000) {
							prompt = "Array Overflow! Please restart the application";
							String html1 = "<html><body style='width: ";
					        String html2 = "px'>";
							
					        myChart.device_label.setForeground(Color.red);
							myChart.device_label.setText(html1 + "250" + html2 + prompt );
							
							return;
						}
						
						samples[count] = value;
						count++;
						
						int temp = Math.max(0, count - 100);
						double min = samples[count-1];
						double max = samples[count-1];
						
						for (int j = count - 1; j >= temp; j-- ) {
							if (samples[j] > -5000 && samples[j] < min) {
								min = samples[j];
							}
							
							if (samples[j] < 5000 && samples[j] > max) {
								max = samples[j];
							}
						}
						
						myChart.minimum = min;
						myChart.maximum = max;
						
/*						if(myChart.maximum == 0 && myChart.minimum == 0){
							myChart.maximum = value;
							myChart.minimum = value;
						} else { 
							if( value > myChart.maximum && value <= 5000 ){
								myChart.maximum = value;
							} else {
								if( value < myChart.minimum && value >= -5000 ){
									myChart.minimum = value;
								}
							}
						}*/
					} else {
						if(s.startsWith("addPeak")){
							StringTokenizer st2 = new StringTokenizer(s, ",");
							st2.nextToken();
							
							long timestamp = Long.parseLong(st2.nextToken());
							double value = Double.parseDouble(st2.nextToken());
							
							myChart.series2.add(timestamp,value);
							myChart.series3.add(timestamp, value);
						} else {
							if(s.startsWith("deletePeak")) {
								StringTokenizer st2 = new StringTokenizer(s, ",");
								st2.nextToken();
								
								long timestamp = Long.parseLong(st2.nextToken());
								
								if (myChart.series3.indexOf((Number)timestamp) >= 0) {
									myChart.series3.remove((Number)timestamp);
								}
							} else {
								if(s.startsWith("addZeroCross")) {
									StringTokenizer st2 = new StringTokenizer(s, ",");
									st2.nextToken();
									
									long timestamp = Long.parseLong(st2.nextToken());
									double value = Double.parseDouble(st2.nextToken());
									
									myChart.series4.add(timestamp, value);
									myChart.series5.add(timestamp, value);
								} else {
									if(s.startsWith("deleteZeroCross")) {
										StringTokenizer st2 = new StringTokenizer(s, ",");
										st2.nextToken();
										
										long timestamp = Long.parseLong(st2.nextToken());
										
										if (myChart.series5.indexOf((Number)timestamp) >= 0) {
											myChart.series5.remove((Number)timestamp);
										}
									} else {
										if(s.startsWith("addBpm")){
											StringTokenizer st2 = new StringTokenizer(s, ",");
											st2.nextToken();
											
											long timestamp = Long.parseLong(st2.nextToken());
											int value = Integer.parseInt(st2.nextToken());
											
											myChart.series6.add(timestamp, value);
											
											if(value >= 50 && value <= 150){
												myChart.hr_label.setText(""+value);
											}
										} else {
											if(s.startsWith("addRawBpm")){
												StringTokenizer st2 = new StringTokenizer(s, ",");
												st2.nextToken();
												
												long timestamp = Long.parseLong(st2.nextToken());
												int value = Integer.parseInt(st2.nextToken());
												
												myChart.series7.add(timestamp, value);
											} else {
												if(s.startsWith("onCovered")){
													prompt = "Lens is covered. Preprocessing the signal in 5 seconds";
													String html1 = "<html><body style='width: ";
											        String html2 = "px'>";
													
											        myChart.lens_label.setForeground(Color.black);
													myChart.lens_label.setText(html1 + "250" + html2 + prompt );
												} else {
													if(s.startsWith("onUncovered")){
														prompt = "Lens is not covered. ";
														String html1 = "<html><body style='width: ";
												        String html2 = "px'>";
														
												        myChart.lens_label.setForeground(Color.red);
														myChart.lens_label.setText(html1 + "250" + html2 + prompt );
														prompt = "--";
														myChart.hr_label.setText(prompt );
														myChart.hr_label.setHorizontalAlignment(JLabel.RIGHT);
														
													} else {
														if(s.startsWith("onBeat")){
															prompt = "Measuring heart rate";
															String html1 = "<html><body style='width: ";
													        String html2 = "px'>";
															
													        myChart.lens_label.setForeground(Color.green);
															myChart.lens_label.setText(html1 + "250" + html2 + prompt );
														} else {
															if(s.startsWith("AppClosed") || s.startsWith("AppPaused")){
//																prompt = "LivePulse app disconnected!";
//																String html1 = "<html><body style='width: ";
//														        String html2 = "px'>";
																
//														        myChart.device_label.setForeground(Color.red);
//																myChart.device_label.setText(html1 + "250" + html2 + prompt );
																
																Timer timer = new Timer(100, action);
														        timer.setRepeats(false);
														        timer.restart();
															} else {
																if(s.startsWith("AppStopped")){
																	myChart.reset();
																	
																	prompt = "You pressed the Stop button. Press the Start button to measure your heart rate.";
																	String html1 = "<html><body style='width: ";
															        String html2 = "px'>";
																	
															        myChart.device_label.setForeground(Color.black);
																	myChart.device_label.setText(html1 + "250" + html2 + prompt );
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	static Action action = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			myChart.reset();
		}
	};
	
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
        
		System.out.println("logcat called\n");
		
		Process process = null;
	    
	    try{
	    	process = Runtime.getRuntime().exec("adb logcat -c");
	    	System.out.println("Logcat cleaned");
	    	
	    	process = null;

	    	ArrayList<String> commandLine = new ArrayList<String>();
			commandLine.clear();   
	    	commandLine.add("adb");
	    	commandLine.add("logcat");
	    	commandLine.add("HeartBeatAlgorithm:I");
	    	commandLine.add("*:S");
	    	
	    	process = Runtime.getRuntime().exec(commandLine.toArray(new String[commandLine.size()]));   
	    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024); 
	    	System.out.println("Logcat read");
	    	
	    	String line = "";
	    	while ( (line = bufferedReader.readLine())!= null){
	    		System.out.println(line);
	    		process(line);
	    	}
	    } catch(Exception ex){
	    	System.out.println(ex.toString());
	    	ex.printStackTrace();
	    	System.out.println("error in Collector_logcat\n");
	    }
	    
	    return ;
	}
}
